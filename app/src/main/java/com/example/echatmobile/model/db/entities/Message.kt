package com.example.echatmobile.model.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Message(
    @PrimaryKey val id: Long,
    val text: String,
    val created: String,
    val questionId: Long?,
    val chatId: Long,
    val senderId: Long
)