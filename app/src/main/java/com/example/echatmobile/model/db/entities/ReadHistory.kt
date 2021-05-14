package com.example.echatmobile.model.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ReadHistory(
    @PrimaryKey val id: Long,
    val messageId: Long,
    val status: String
) {
    companion object {
        const val READ = "read"
        const val NOT_READ = "not_read"
    }
}