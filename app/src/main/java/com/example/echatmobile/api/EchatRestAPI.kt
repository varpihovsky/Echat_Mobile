package com.example.echatmobile.api

import com.example.echatmobile.model.entities.Authorization
import com.example.echatmobile.model.entities.User
import com.example.echatmobile.model.entities.UserWithoutPassword
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface EchatRestAPI {
    @GET("authorization/authorize")
    fun getAuthorizationKey(
        @Query("login") login: String,
        @Query("password") password: String
    ): Call<Authorization>

    @GET("profile/get/by-key")
    fun getProfileByKey(@Query("key") key: String): Call<User>

    @GET("profile/get/by-id")
    fun getProfileById(
        @Query("key") key: String,
        @Query("id") id: Int
    ): Call<UserWithoutPassword>

    @POST("profile/register")
    fun register(
        @Query("login") login: String,
        @Query("password") password: String
    ): Call<Any>
}