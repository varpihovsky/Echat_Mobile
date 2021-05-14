package com.example.echatmobile.model.db.entities

import androidx.room.Entity

@Entity(primaryKeys = ["chatId", "userId"])
data class ChatParticipantsCrossRef(
    val chatId: Long,
    val userId: Long
)