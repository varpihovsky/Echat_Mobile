package com.example.echatmobile.model

import android.content.Context
import android.util.Log
import com.example.echatmobile.api.EchatRestAPI
import com.example.echatmobile.model.entities.*
import com.example.echatmobile.system.ConnectionManager
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class EchatModel @Inject constructor(private val echatRestAPI: EchatRestAPI) {
    val currentUserLogin: String
        get() = authorizationSaver.getLoginAndPassword().first

    private val authorizationKey: Authorization
        get() = authorizationSaver.getAuthorization()

    @Inject
    lateinit var context: Context

    private val authorizationSaver by lazy { EchatAuthorizationSaver(context) }

    fun isLoginPresent(): Boolean =
        try {
            authorizationSaver.getLoginAndPassword().let {
                authorize(it.first, it.second)
            }
            true
        } catch (e: Exception) {
            false
        }

    fun logout() {
        authorizationSaver.clear()
    }

    fun authorize(login: String, password: String) {
        checkInternetConnection()
        val response = echatRestAPI.getAuthorizationKey(login, password).execute()
        if (!response.isSuccessful) {
            throw WrongLoginOrPasswordException("Wrong login or password")
        }

        response.body()?.let {
            Log.d(DEBUG_PREFIX, "Authorization key: $it")
            saveAuthorizationData(it)
            authorizationSaver.saveLoginAndPassword(login, password)
        }
    }

    fun register(login: String, password: String) {
        checkInternetConnection()

        val response = echatRestAPI.register(login, password).execute()
        if (!response.isSuccessful) {
            throw IllegalArgumentException("User with this login exists or password length is less than 8")
        }
    }

    fun getCurrentUserProfile(): User? {
        val response =
            executeCallAndCheckForErrors { echatRestAPI.getProfileByKey(authorizationKey.key) }
        return response?.body()
    }

    fun getUserProfileById(id: Long): UserWithoutPassword? {
        val response =
            executeCallAndCheckForErrors { echatRestAPI.getProfileById(authorizationKey.key, id) }
        return response?.body()
    }

    fun getUserProfileByQuery(query: String): List<UserWithoutPassword> {
        val currentUser = getCurrentUserProfile()?.let { UserWithoutPassword(it.id, it.login) }
        val response = executeCallAndCheckForErrors {
            echatRestAPI.getProfilesByQuery(authorizationKey.key, query)
        }
        return response?.body()?.response?.filter { it != currentUser } ?: emptyList()
    }

    fun getChatsByParticipantId(id: Long): List<Chat> {
        val response = executeCallAndCheckForErrors {
            echatRestAPI.getChatsByParticipantId(
                authorizationKey.key,
                id
            )
        }
        return response?.body()?.response ?: emptyList()
    }

    fun getChatsByQuery(query: String): List<Chat> {
        val response = executeCallAndCheckForErrors(2) {
            echatRestAPI.getChatsByQuery(authorizationKey.key, query)
        }
        return response?.body()?.response ?: emptyList()
    }

    fun createChat(name: String): Chat? {
        val response =
            executeCallAndCheckForErrors(1) { echatRestAPI.createChat(authorizationKey.key, name) }
        return response?.body()
    }

    fun joinToChat(chatId: Long) {
        executeCallAndCheckForErrors { echatRestAPI.joinToChat(authorizationKey.key, chatId) }
    }

    fun getMessageHistory(chatId: Long): List<Message> {
        val response = executeCallAndCheckForErrors {
            echatRestAPI.getMessageHistory(
                authorizationKey.key,
                chatId
            )
        }
        return response?.body()?.response ?: emptyList()
    }

    fun writeMessage(chatId: Long, text: String, toMessageId: Long? = null) {
        if (toMessageId == null) {
            executeCallAndCheckForErrors(1) {
                echatRestAPI.writeMessage(authorizationKey.key, chatId, text)
            }
        } else {
            executeCallAndCheckForErrors(1) {
                echatRestAPI.writeMessage(authorizationKey.key, chatId, text, toMessageId)
            }
        }
    }

    fun setMessageRead(messageId: Long) {
        executeCallAndCheckForErrors {
            echatRestAPI.setMessageRead(
                authorizationKey.key,
                messageId
            )
        }
    }

    fun getNotReadMessages(): List<Message> {
        val response = executeCallAndCheckForErrors {
            echatRestAPI.getNotReadMessages(authorizationKey.key)
        }
        return response?.body()?.response ?: emptyList()
    }

    fun getCurrentUserChatList() = getCurrentUserProfile()?.id?.let { getChatsByParticipantId(it) }

    fun getCurrentUserInvites(): List<Invite> {
        val response =
            executeCallAndCheckForErrors {
                echatRestAPI.getInvites(authorizationKey.key)
            }
        return response?.body()?.response ?: emptyList()
    }

    fun invite(chatId: Long, userId: Long) {
        executeCallAndCheckForErrors {
            echatRestAPI.invite(authorizationKey.key, chatId, userId)
        }
    }

    fun acceptInvite(inviteId: Long) {
        executeCallAndCheckForErrors {
            echatRestAPI.acceptInvite(authorizationKey.key, inviteId)
        }
    }

    fun declineInvite(inviteId: Long) {
        executeCallAndCheckForErrors {
            echatRestAPI.declineInvite(authorizationKey.key, inviteId)
        }
    }

    private fun <T> executeCallAndCheckForErrors(
        repeatCount: Int = 2,
        block: () -> Call<T>
    ): Response<T>? {
        var response: Response<T>? = null

        repeat(repeatCount) {
            try {
                checkInternetConnection()
                response = block.invoke().execute()
                return@repeat
            } catch (e: Exception) {
                Log.d(DEBUG_PREFIX, e.message, e)
                reauthorize(response)

                if (it == repeatCount) {
                    throw e
                }
            }
            checkError(response)
        }
        return response
    }

    private fun checkInternetConnection() {
        if (!ConnectionManager().isConnected()) {
            throw NoInternetConnectionException("Please connect to the Internet")
        }
    }

    private fun <T> checkError(response: Response<T>?) {
        if (response?.isSuccessful == false) {
            throw RuntimeException(response.errorBody()?.string() + "\n" + response.message())
        }
    }

    private fun <T> reauthorize(response: Response<T>?) {
        if (response?.code() == FORBIDDEN) {
            authorizationSaver.getLoginAndPassword().let {
                authorize(it.first, it.second)
            }
        }
    }


    private fun saveAuthorizationData(authorization: Authorization) {
        authorizationSaver.saveAuthorization(authorization)
    }

    companion object {
        private const val DEBUG_PREFIX = "Application/Model"
        private const val FORBIDDEN = 403
    }
}