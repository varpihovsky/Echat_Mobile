package com.example.echatmobile.model

import android.util.JsonReader
import android.util.Log
import com.example.echatmobile.api.EchatRestAPI
import com.example.echatmobile.system.ConnectionManager
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Inject

class EchatModel @Inject constructor(private val echatRestAPI: EchatRestAPI) {
    private lateinit var authorizationKey: Any

    fun authorize(login: String, password: String) {
        checkInternetConnection()

        val response = echatRestAPI.getAuthorizationKey(login, password).execute()
        if (!response.isSuccessful) {
            throw WrongLoginOrPasswordException("Wrong login or password")
        }
        response.body()?.let {
            authorizationKey = it.key
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

    companion object {
        private const val DEBUG_PREFIX = "Application/Model"
    }
}