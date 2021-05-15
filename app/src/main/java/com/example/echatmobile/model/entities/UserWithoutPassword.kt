package com.example.echatmobile.model.entities

import com.example.echatmobile.model.db.entities.User

data class UserWithoutPassword(
    val id: Long,
    val login: String
) : DBConvertibleUser {
    override fun toDBObj() = User(id, login)
}