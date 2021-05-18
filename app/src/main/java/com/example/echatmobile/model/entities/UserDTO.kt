package com.example.echatmobile.model.entities

import com.example.echatmobile.model.db.entities.User

data class UserDTO(
    val authorization: Authorization,
    val id: Long,
    val login: String,
    val password: String
) : DBConvertibleUser {
    override fun toDBObj() = User(id, login)
}