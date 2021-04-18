package com.example.echatmobile.model.entities

data class User(
    val authorization: Authorization,
    val id: Long,
    val login: String,
    val password: String
)