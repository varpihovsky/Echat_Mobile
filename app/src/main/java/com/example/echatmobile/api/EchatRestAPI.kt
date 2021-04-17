package com.example.echatmobile.api

import com.example.echatmobile.model.entities.AuthorizationKey
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface EchatRestAPI {
    @GET("authorization/authorize")
    fun getAuthorizationKey(
        @Query("login") login: String,
        @Query("password") password: String
    ): Call<AuthorizationKey>

    @POST("profile/register")
    fun register(
        @Query("login") login: String,
        @Query("password") password: String
    ): Call<Any>
}