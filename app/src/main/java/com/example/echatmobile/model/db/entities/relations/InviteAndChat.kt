package com.example.echatmobile.model.db.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.echatmobile.model.db.entities.Chat
import com.example.echatmobile.model.db.entities.Invite

data class InviteAndChat(
    @Embedded val invite: Invite,
    @Relation(
        parentColumn = "chatId",
        entityColumn = "id"
    ) val chat: Chat
)