package com.example.echatmobile.model.remote

import com.example.echatmobile.api.EchatRestAPI
import com.example.echatmobile.model.NoInternetConnectionException
import com.example.echatmobile.model.UnauthorizedException
import com.example.echatmobile.model.entities.*
import com.example.echatmobile.system.ConnectionManager
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class EchatRemote @Inject constructor(
    private val echatRestAPI: EchatRestAPI,
    private val connectionManager: ConnectionManager
) {
    private lateinit var authorizationKey: Authorization

    fun setAuthorization(authorization: Authorization) {
        this.authorizationKey = authorization
    }

    fun isAuthorizationNull() = !this::authorizationKey.isInitialized

    fun getAuthorization(login: String, password: String): Authorization? {
        checkInternetConnection()
        val response = echatRestAPI.getAuthorizationKey(login, password).execute()
        checkError(response)
        return response.body()
    }

    fun register(login: String, password: String) {
        checkInternetConnection()

        val response = echatRestAPI.register(login, password).execute()
        if (!response.isSuccessful) {
            throw IllegalArgumentException("User with this login exists or password length is less than 8")
        }
    }

    fun getUserProfileById(id: Long): UserWithoutPassword? {
        val response =
            executeCallAndCheckForErrors { echatRestAPI.getProfileById(authorizationKey.key, id) }
        return response.body()
    }

    fun getUserProfileByAuthorizationKey(authorization: Authorization): UserDTO? {
        val response =
            executeCallAndCheckForErrors { echatRestAPI.getProfileByKey(authorization.key) }
        return response.body()
    }

    fun getUserProfileByQuery(query: String): List<UserWithoutPassword> {
        val response = executeCallAndCheckForErrors {
            echatRestAPI.getProfilesByQuery(authorizationKey.key, query)
        }
        return response.body()?.response ?: emptyList()
    }

    fun getChatsByParticipantId(id: Long): List<ChatDTO> {
        val response = executeCallAndCheckForErrors {
            echatRestAPI.getChatsByParticipantId(
                authorizationKey.key,
                id
            )
        }
        return response.body()?.response ?: emptyList()
    }

    fun getChatsByQuery(query: String): List<ChatDTO> {
        val response = executeCallAndCheckForErrors {
            echatRestAPI.getChatsByQuery(authorizationKey.key, query)
        }
        return response.body()?.response ?: emptyList()
    }

    fun createChat(name: String): ChatDTO? {
        val response =
            executeCallAndCheckForErrors { echatRestAPI.createChat(authorizationKey.key, name) }
        return response.body()
    }

    fun joinToChat(chatId: Long): ChatDTO? {
        val response =
            executeCallAndCheckForErrors { echatRestAPI.joinToChat(authorizationKey.key, chatId) }
        return response.body()
    }

    fun getMessageHistory(chatId: Long): List<MessageDTO> {
        val response = executeCallAndCheckForErrors {
            echatRestAPI.getMessageHistory(
                authorizationKey.key,
                chatId
            )
        }
        return response.body()?.response ?: emptyList()
    }

    fun writeMessage(chatId: Long, text: String, toMessageId: Long? = null) {
        if (toMessageId == null) {
            executeCallAndCheckForErrors {
                echatRestAPI.writeMessage(authorizationKey.key, chatId, text)
            }
        } else {
            executeCallAndCheckForErrors {
                echatRestAPI.writeMessage(authorizationKey.key, chatId, text, toMessageId)
            }
        }
    }

    fun setMessageRead(messageId: Long) {
        try {
            executeCallAndCheckForErrors {
                echatRestAPI.setMessageRead(
                    authorizationKey.key,
                    messageId
                )
            }
        } catch (e: Exception) {
        }
    }

    fun getNotReadMessages(): List<MessageDTO> {
        val response = executeCallAndCheckForErrors {
            echatRestAPI.getNotReadMessages(authorizationKey.key)
        }
        return response.body()?.response ?: emptyList()
    }

    fun getCurrentUserInvites(): List<InviteDTO> {
        val response =
            executeCallAndCheckForErrors {
                echatRestAPI.getInvites(authorizationKey.key)
            }
        return response.body()?.response ?: emptyList()
    }

    fun invite(chatId: Long, userId: Long) {
        try {
            executeCallAndCheckForErrors {
                echatRestAPI.invite(
                    authorizationKey.key,
                    chatId,
                    userId
                )
            }
        } catch (e: RuntimeException) {
            throw RuntimeException("Only admins can invite new users to the chat")
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
        block: () -> Call<T>
    ): Response<T> {
        checkInternetConnection()
        val response: Response<T> = block.invoke().execute()
        checkError(response)
        return response
    }

    private fun checkInternetConnection() {
        if (!connectionManager.isConnected()) {
            throw NoInternetConnectionException("Please connect to the Internet")
        }
    }

    private fun <T> checkError(response: Response<T>?) {
        if (response?.isSuccessful == false) {
            if (response.code() == 403) {
                throw UnauthorizedException("Authorization invalidated (probably somebody logged in to your account)")
            }
            if (response.code() == 406) {
                throw RuntimeException("Action is not acceptable")
            }
            throw RuntimeException(response.code().toString())
        }

    }
}