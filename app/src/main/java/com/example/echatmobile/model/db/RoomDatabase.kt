package com.example.echatmobile.model.db

import androidx.room.Database
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
    version = 1
)
abstract class RoomDatabase {
    abstract val roomDAO: RoomDAO
}