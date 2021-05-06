package com.example.echatmobile.model.entities

data class Invite(
    val id: Long,
    val account: UserWithoutPassword,
    val chat: Chat
)