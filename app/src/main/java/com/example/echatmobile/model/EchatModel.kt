package com.example.echatmobile.model

import com.example.echatmobile.model.db.EchatDatabase
import com.example.echatmobile.model.entities.*
import com.example.echatmobile.model.remote.EchatRemote
import javax.inject.Inject

class EchatModel @Inject constructor(
    private val echatRemote: EchatRemote,
    private val echatDatabase: EchatDatabase,
    private val authorizationSaver: EchatAuthorizationSaver
) {
    val currentUserLogin: String
        get() = authorizationSaver.getLoginAndPassword().first

    private val authorizationKey: Authorization
        get() = authorizationSaver.getAuthorization()

    fun isLoginPresent(): Boolean =
        try {
            authorizationSaver.getLoginAndPassword().let {
                try {
                    authorize(it.first, it.second)
                } catch (e: NoInternetConnectionException) {
                }
            }
            true
        } catch (e: Exception) {
            false
        }

    fun logout() {
        authorizationSaver.clear()
    }

    fun authorize(login: String, password: String) {
        val authorization = echatRemote.getAuthorization(login, password)?.also {
            authorizationSaver.saveAuthorization(it)
            authorizationSaver.saveLoginAndPassword(login, password)
            echatRemote.setAuthorization(it)
        }
        isWrong(authorization, "Wrong login or password")
    }

    fun register(login: String, password: String) {
        echatRemote.register(login, password)
    }

    fun getCurrentUserProfile(): UserWithoutPassword? {
        var userDTO: UserWithoutPassword? = null
        try {
            echatRemote.getUserProfileByAuthorizationKey(authorizationKey)?.let {
                saveCurrentUser(it)
                userDTO = UserWithoutPassword(it.id, it.login)
            }
        } catch (e: NoInternetConnectionException) {
            try {
                userDTO = echatDatabase.getUserById(authorizationSaver.getCurrentUserId())
            } catch (e: Exception) {
                throw e
            }
        }
        return userDTO
    }

    private fun saveCurrentUser(userDTO: UserDTO) {
        authorizationSaver.saveCurrentUserId(userDTO.id)
        echatDatabase.addUser(userDTO)
    }

    fun getUserProfileById(id: Long): UserWithoutPassword? {
        return echatRemote.getUserProfileById(id)
    }

    fun getUserProfileByQuery(query: String): List<UserWithoutPassword> {
        return echatRemote.getUserProfileByQuery(query).filter { it != getCurrentUserProfile() }
    }

    fun getChatsByParticipantId(id: Long): List<ChatDTO> {
        var chats: List<ChatDTO> = listOf()
        try {
            chats = echatRemote.getChatsByParticipantId(id)
            if (isUserIdEqualsToCurrentUser(id)) {
                chats.forEach { echatDatabase.addChat(it) }
            }
        } catch (e: NoInternetConnectionException) {
            if (isUserIdEqualsToCurrentUser(id)) {
                chats = echatDatabase.getChatsByUserId(id)
            }
        }
        return chats
    }

    fun getChatsByQuery(query: String): List<ChatDTO> {
        return echatRemote.getChatsByQuery(query)
    }

    fun createChat(name: String): ChatDTO? {
        return echatRemote.createChat(name)?.also {
            echatDatabase.addChat(it)
        }
    }

    fun joinToChat(chatId: Long) {
        echatRemote.joinToChat(chatId)?.let {
            echatDatabase.addChat(it)
        }
    }

    fun getMessageHistory(chatId: Long): List<MessageDTO> {
        var list: List<MessageDTO> = listOf()
        try {
            list = echatRemote.getMessageHistory(chatId).onEach { messageDTO ->
                echatDatabase.addMessage(messageDTO)
            }
        } catch (e: NoInternetConnectionException) {
            list = echatDatabase.getMessagesByChat(chatId)
        }
        return list
    }

    fun writeMessage(chatId: Long, text: String, toMessageId: Long? = null) {
        echatRemote.writeMessage(chatId, text, toMessageId)
    }

    fun setMessageRead(messageId: Long) {
        echatRemote.setMessageRead(messageId)
        echatDatabase.setMessageRead(messageId)
    }

    fun setMessageReadLocal(messageId: Long) {
        echatDatabase.setMessageRead(messageId)
    }

    fun getNotReadMessagesLocal(): List<MessageDTO> =
        echatDatabase.getNotReadMessages()

    fun getNotReadMessages(): List<MessageDTO> {
        return echatRemote.getNotReadMessages()
    }

    fun getCurrentUserChatList() = getCurrentUserProfile()?.id?.let { getChatsByParticipantId(it) }

    fun getCurrentUserInvites(): List<InviteDTO> {
        return echatRemote.getCurrentUserInvites().also {
            it.forEach { inviteDTO -> echatDatabase.addInvite(inviteDTO) }
        }
    }

    fun invite(chatId: Long, userId: Long) {
        echatRemote.invite(chatId, userId)
    }

    fun acceptInvite(inviteId: Long) {
        echatRemote.acceptInvite(inviteId)
        echatDatabase.removeInviteById(inviteId)
    }

    fun declineInvite(inviteId: Long) {
        echatRemote.declineInvite(inviteId)
        echatDatabase.removeInviteById(inviteId)
    }

    private fun isWrong(any: Any?, errorText: String = "Something went wrong") {
        if (any == null) {
            throw RuntimeException(errorText)
        }
    }

    private fun isUserIdEqualsToCurrentUser(id: Long) = id == authorizationSaver.getCurrentUserId()
}