package com.example.echatmobile.model.entities

import com.example.echatmobile.model.db.entities.Chat

data class ChatDTO(
    val id: Long,
    val name: String,
    val chatAdmins: List<UserWithoutPassword>,
    val chatParticipants: List<UserWithoutPassword>,
    val type: String = "OPEN"
) {
    fun toDBObj() = Chat(id, name)
}