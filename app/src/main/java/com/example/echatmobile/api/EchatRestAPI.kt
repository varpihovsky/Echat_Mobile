package com.example.echatmobile.api

import com.example.echatmobile.model.entities.*
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

    @POST("profile/register")
    fun register(
        @Query("login") login: String,
        @Query("password") password: String
    ): Call<Any>

    @GET("profile/get/by-key")
    fun getProfileByKey(@Query("key") key: String): Call<User>

    @GET("profile/get/by-id")
    fun getProfileById(
        @Query("key") key: String,
        @Query("id") id: Long
    ): Call<UserWithoutPassword>

    @GET("chat/get/by-participant")
    fun getChatsByParticipantId(
        @Query("key") key: String,
        @Query("id") participantId: Long
    ): Call<ChatList>

    @POST("chat/create")
    fun createChat(
        @Query("key") key: String,
        @Query("name") name: String
    ): Call<Chat>
}