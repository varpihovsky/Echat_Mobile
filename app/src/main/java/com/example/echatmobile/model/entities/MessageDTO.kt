package com.example.echatmobile.model.entities

import com.example.echatmobile.model.db.entities.Message

data class MessageDTO(
    val id: Long,
    val text: String,
    val sender: UserWithoutPassword,
    val chat: ChatDTO,
    val `receiver`: MessageDTO?,
    val created: String,

    ) {
    fun toDBObj() = Message(id, text, created, receiver?.id, chat.id, sender.id)
}