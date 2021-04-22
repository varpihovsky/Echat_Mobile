package com.example.echatmobile.chat

import com.example.echatmobile.model.entities.Chat
import com.example.echatmobile.model.entities.Message
import com.example.echatmobile.model.entities.UserWithoutPassword

data class MessageDTO(
    val chat: Chat,
    val created: String,
    val id: Long,
    val receiver: MessageDTO?,
    val sender: UserWithoutPassword,
    val text: String,
    val align: String
)

const val ALIGN_LEFT = "left"
const val ALIGN_RIGHT = "right"

fun Message.toDTO(align: (Message) -> String): MessageDTO {
    val receiverDTO = receiver?.toDTO(align)
    return MessageDTO(chat, created, id, receiverDTO, sender, text, align.invoke(this))
}