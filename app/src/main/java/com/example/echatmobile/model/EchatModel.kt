package com.example.echatmobile.model

import android.util.Log
import com.example.echatmobile.api.EchatRestAPI
import com.example.echatmobile.model.entities.*
import com.example.echatmobile.system.ConnectionManager
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class EchatModel @Inject constructor(private val echatRestAPI: EchatRestAPI) {
    val currentUserLogin: String
        get() = login

    private lateinit var authorizationKey: Authorization
    private lateinit var login: String
    private lateinit var password: String

    fun authorize(login: String, password: String) {
        checkInternetConnection()

        this.login = login
        this.password = password

        val response = echatRestAPI.getAuthorizationKey(login, password).execute()
        if (!response.isSuccessful) {
            throw WrongLoginOrPasswordException("Wrong login or password")
        }
        response.body()?.let {
            authorizationKey = it
            Log.d(DEBUG_PREFIX, "Authorization key: $authorizationKey")
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

    fun getChatsByParticipantId(id: Long): List<Chat> {
        val response = executeCallAndCheckForErrors {
            echatRestAPI.getChatsByParticipantId(
                authorizationKey.key,
                id
            )
        }
        return response?.body()?.response ?: emptyList()
    }

    fun createChat(name: String): Chat? {
        val response =
            executeCallAndCheckForErrors { echatRestAPI.createChat(authorizationKey.key, name) }
        return response?.body()
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
                reauthorize(response)
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
            authorize(login, password)
        }
    }

    companion object {
        private const val DEBUG_PREFIX = "Application/Model"
        private const val FORBIDDEN = 403
    }
}