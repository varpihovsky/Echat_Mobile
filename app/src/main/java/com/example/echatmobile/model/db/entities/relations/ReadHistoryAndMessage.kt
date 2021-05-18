package com.example.echatmobile.model.db.entities.relations

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation
import com.example.echatmobile.model.db.entities.Message
import com.example.echatmobile.model.db.entities.ReadHistory

@Entity
data class ReadHistoryAndMessage(
    @Embedded val readHistory: ReadHistory,
    @Relation(
        parentColumn = "messageId",
        entityColumn = "id"
    ) val message: Message
)