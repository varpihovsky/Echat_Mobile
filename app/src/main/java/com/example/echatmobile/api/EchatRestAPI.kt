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
    fun getProfileByKey(@Query("key") key: String): Call<UserDTO>

    @GET("profile/get/by-id")
    fun getProfileById(
        @Query("key") key: String,
        @Query("id") id: Long
    ): Call<UserWithoutPassword>

    @GET("profile/get/by-name")
    fun getProfilesByQuery(
        @Query("key") key: String,
        @Query("name") name: String
    ): Call<ResponseList<UserWithoutPassword>>

    @GET("chat/get/by-participant")
    fun getChatsByParticipantId(
        @Query("key") key: String,
        @Query("id") participantId: Long
    ): Call<ResponseList<ChatDTO>>

    @GET("chat/get/by-name")
    fun getChatsByQuery(
        @Query("key") key: String,
        @Query("name") name: String
    ): Call<ResponseList<ChatDTO>>

    @POST("chat/create")
    fun createChat(
        @Query("key") key: String,
        @Query("name") name: String
    ): Call<ChatDTO>

    @POST("chat/join")
    fun joinToChat(
        @Query("key") key: String,
        @Query("id") chatId: Long
    ): Call<ChatDTO>

    @GET("message/get-history")
    fun getMessageHistory(
        @Query("key") key: String,
        @Query("chat-id") chatId: Long
    ): Call<ResponseList<MessageDTO>>

    @POST("message/write")
    fun writeMessage(
        @Query("key") key: String,
        @Query("chat-id") chatId: Long,
        @Query("text") text: String,
        @Query(value = "to-message-id") toMessageId: Long
    ): Call<Any>

    @POST("message/write")
    fun writeMessage(
        @Query("key") key: String,
        @Query("chat-id") chatId: Long,
        @Query("text") text: String
    ): Call<Any>

    @POST("message/read")
    fun setMessageRead(
        @Query("key") key: String,
        @Query("id") messageId: Long
    ): Call<Any>

    @GET("message/not-read")
    fun getNotReadMessages(@Query("key") key: String): Call<ResponseList<MessageDTO>>

    @GET("invite/get/all")
    fun getInvites(@Query("key") key: String): Call<ResponseList<InviteDTO>>

    @POST("invite")
    fun invite(
        @Query("key") key: String,
        @Query("chat-id") chatId: Long,
        @Query("id") userId: Long
    ): Call<Any>

    @POST("invite/accept")
    fun acceptInvite(
        @Query("key") key: String,
        @Query("id") invitationId: Long
    ): Call<Any>

    @POST("invite/decline")
    fun declineInvite(
        @Query("key") key: String,
        @Query("id") invitationId: Long
    ): Call<Any>
}