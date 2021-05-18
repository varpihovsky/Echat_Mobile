package com.example.echatmobile.model.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity()
data class Chat(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "name") val name: String
)