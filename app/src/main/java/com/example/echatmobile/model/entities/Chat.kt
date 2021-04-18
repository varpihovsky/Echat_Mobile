package com.example.echatmobile.model.entities

data class Chat(
    val chatAdmins: List<UserWithoutPassword>,
    val chatParticipants: List<UserWithoutPassword>,
    val id: Long,
    val name: String
)