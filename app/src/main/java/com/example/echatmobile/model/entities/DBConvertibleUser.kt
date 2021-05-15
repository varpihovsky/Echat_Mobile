package com.example.echatmobile.model.entities

import com.example.echatmobile.model.db.entities.User

interface DBConvertibleUser {
    fun toDBObj(): User
}