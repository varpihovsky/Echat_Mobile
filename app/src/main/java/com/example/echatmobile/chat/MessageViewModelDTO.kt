package com.example.echatmobile.chat

import com.example.echatmobile.model.entities.ChatDTO
import com.example.echatmobile.model.entities.MessageDTO
import com.example.echatmobile.model.entities.UserWithoutPassword

data class MessageViewModelDTO(
    val chatDTO: ChatDTO,
    val created: String,
    val id: Long,
    val receiver: MessageViewModelDTO?,
    val sender: UserWithoutPassword,
    val text: String,
    val align: String
)

const val ALIGN_LEFT = "left"
const val ALIGN_RIGHT = "right"

fun MessageDTO.toDTO(align: (MessageDTO) -> String): MessageViewModelDTO {
    val receiverDTO = receiver?.toDTO(align)
    return MessageViewModelDTO(chat, created, id, receiverDTO, sender, text, align.invoke(this))
}