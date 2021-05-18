package com.example.echatmobile.model.db.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.echatmobile.model.db.entities.Chat
import com.example.echatmobile.model.db.entities.Message
import com.example.echatmobile.model.db.entities.User

data class MessageAndMessageAndChatAndUser(
    @Embedded val message: Message,
    @Relation(
        parentColumn = "questionId",
        entityColumn = "id"
    ) val question: Message?,
    @Relation(
        parentColumn = "chatId",
        entityColumn = "id"
    ) val chat: Chat,
    @Relation(
        parentColumn = "senderId",
        entityColumn = "id"
    ) val sender: User
)