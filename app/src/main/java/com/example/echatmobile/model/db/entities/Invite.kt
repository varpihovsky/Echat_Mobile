package com.example.echatmobile.model.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity()
data class Invite(
    @PrimaryKey val inviteId: Long,
    val chatId: Long
)