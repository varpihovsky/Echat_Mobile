package com.example.echatmobile.model

import com.example.echatmobile.model.db.EchatDatabase
import com.example.echatmobile.model.entities.*
import com.example.echatmobile.model.remote.EchatRemote
import com.example.echatmobile.system.alsoHandleUnblocking
import com.example.echatmobile.system.handleUnblocking
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
        val authorization = echatRemote.getAuthorization(login, password)?.handleUnblocking {
            authorizationSaver.saveAuthorization(it)
            authorizationSaver.saveLoginAndPassword(login, password)
            echatRemote.setAuthorization(it)
        }
        isWrong(authorization, "Wrong login or password")
    }

    fun register(login: String, password: String) {
        echatRemote.register(login, password)
    }

    fun getCurrentUserProfile(): UserWithoutPassword? =
        try {
            echatRemote.getUserProfileByAuthorizationKey(authorizationKey)?.alsoHandleUnblocking {
                saveCurrentUser(it)
            }?.let { UserWithoutPassword(it.id, it.login) }
        } catch (e: NoInternetConnectionException) {
            try {
                echatDatabase.getUserById(authorizationSaver.getCurrentUserId())
            } catch (e: Exception) {
                throw e
            }
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

    fun getChatsByParticipantId(id: Long): List<ChatDTO> =
        try {
            echatRemote.getChatsByParticipantId(id).alsoHandleUnblocking {
                if (isUserIdEqualsToCurrentUser(id)) {
                    it.forEach { chatDTO -> echatDatabase.addChat(chatDTO) }
                }
            }
        } catch (e: NoInternetConnectionException) {
            if (isUserIdEqualsToCurrentUser(id)) {
                echatDatabase.getChatsByUserId(id)
            } else {
                throw RuntimeException()
            }
        }

    fun getChatsByQuery(query: String): List<ChatDTO> {
        return echatRemote.getChatsByQuery(query)
    }

    fun createChat(name: String): ChatDTO? {
        return echatRemote.createChat(name)?.alsoHandleUnblocking {
            echatDatabase.addChat(it)
        }
    }

    fun joinToChat(chatId: Long) {
        echatRemote.joinToChat(chatId)?.let {
            echatDatabase.addChat(it)
        }
    }

    fun getMessageHistory(chatId: Long): List<MessageDTO> =
        try {
            echatRemote.getMessageHistory(chatId).alsoHandleUnblocking {
                it.forEach { messageDTO -> echatDatabase.addMessage(messageDTO) }
            }
        } catch (e: NoInternetConnectionException) {
            echatDatabase.getMessagesByChat(chatId)
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

    fun getReadMessagesLocal(): List<MessageDTO> =
        echatDatabase.getReadMessages()

    fun getNotReadMessages(): List<MessageDTO> {
        return echatRemote.getNotReadMessages().alsoHandleUnblocking {
            it.forEach { message -> echatDatabase.addMessage(message) }
        }
    }

    fun getCurrentUserChatList() = getCurrentUserProfile()?.id?.let {
        getChatsByParticipantId(it)
    }

    fun getCurrentUserInvites(): List<InviteDTO> {
        return echatRemote.getCurrentUserInvites()
            .onEach { inviteDTO -> echatDatabase.addInvite(inviteDTO) }
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