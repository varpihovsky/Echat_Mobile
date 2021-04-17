package com.example.echatmobile.model

import android.util.JsonReader
import android.util.Log
import com.example.echatmobile.api.EchatRestAPI
import com.example.echatmobile.model.entities.Authorization
import com.example.echatmobile.model.entities.User
import com.example.echatmobile.model.entities.UserWithoutPassword
import com.example.echatmobile.system.ConnectionManager
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.lang.RuntimeException
import javax.inject.Inject

class EchatModel @Inject constructor(private val echatRestAPI: EchatRestAPI) {
    private lateinit var authorizationKey: Authorization

    fun authorize(login: String, password: String) {
        checkInternetConnection()

        val response = echatRestAPI.getAuthorizationKey(login, password).execute()
        if (!response.isSuccessful) {
            throw WrongLoginOrPasswordException("Wrong login or password")
        }
        response.body()?.let {
            authorizationKey = it
            Log.d(DEBUG_PREFIX, "Authorization key: $authorizationKey")
        }
    }

    fun register(login: String, password: String){
        checkInternetConnection()

        val response = echatRestAPI.register(login, password).execute()
        if(!response.isSuccessful){
            throw IllegalArgumentException("User with this login exists or password length is less than 8")
        }
    }

    private fun checkInternetConnection() {
        if (!ConnectionManager().isConnected()) {
            throw NoInternetConnectionException("Please connect to the Internet")
        }
    }

    fun getCurrentUserProfile(): User? {
        checkInternetConnection()
        val response = echatRestAPI.getProfileByKey(authorizationKey.key).execute()

        if(!response.isSuccessful){
            throw RuntimeException(response.errorBody()?.string() + "\n" + response.message())
        }
        return response.body()
    }

    fun getUserProfileById(id: Int): UserWithoutPassword?{
        checkInternetConnection()
        val response = echatRestAPI.getProfileById(authorizationKey.key, id).execute()

        if(!response.isSuccessful){
            throw RuntimeException(response.errorBody()?.string() + "\n" + response.message())
        }
        return response.body()
    }

    companion object {
        private const val DEBUG_PREFIX = "Application/Model"
    }
}