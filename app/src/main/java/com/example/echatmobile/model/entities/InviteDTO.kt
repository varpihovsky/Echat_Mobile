package com.example.echatmobile.model.entities

import com.example.echatmobile.model.db.entities.Invite

data class InviteDTO(
    val id: Long,
    val account: UserWithoutPassword,
    val chatDTO: ChatDTO
) {
    fun toDBObj() = Invite(id, chatDTO.id)
}