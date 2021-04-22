package com.example.echatmobile.model.entities

data class Message(
    val chat: Chat,
    val created: String,
    val id: Long,
    val `receiver`: Message?,
    val sender: UserWithoutPassword,
    val text: String
)