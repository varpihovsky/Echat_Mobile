package com.example.echatmobile.model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.echatmobile.model.db.entities.*

@Database(
    entities = [
        Chat::class,
        ChatAdminsCrossRef::class,
        ChatParticipantsCrossRef::class,
        Invite::class,
        Message::class,
        ReadHistory::class,
        User::class
    ],
    version = 2,
    exportSchema = false
)
abstract class EchatRoomDatabase : RoomDatabase() {
    abstract val roomDAO: RoomDAO
}