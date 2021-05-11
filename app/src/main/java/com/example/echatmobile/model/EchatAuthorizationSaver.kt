package com.example.echatmobile.model

import android.content.Context
import com.example.echatmobile.model.entities.Authorization

class EchatAuthorizationSaver(private val context: Context) {
    private val preferences by lazy {
        context.getSharedPreferences(
            PREFERENCES,
            Context.MODE_PRIVATE
        )
    }

    fun saveAuthorization(authorization: Authorization) {
        with(preferences.edit()) {
            putString(KEY_STRING, authorization.key)
            putString(CREATED_STRING, authorization.created)
            putLong(ID_LONG, authorization.id)
            apply()
        }
    }

    fun getAuthorization(): Authorization {
        val id = preferences.getLong(ID_LONG, -1)
            .let { if (it == -1L) throwWasNotSavedException() else it }

        return Authorization(
            preferences.getString(CREATED_STRING, null) ?: throwWasNotSavedException(),
            id,
            preferences.getString(KEY_STRING, null) ?: throwWasNotSavedException()
        )
    }

    fun saveLoginAndPassword(login: String, password: String) {
        with(preferences.edit()) {
            putString(LOGIN_STRING, login)
            putString(PASSWORD_STRING, password)
            apply()
        }
    }

    fun getLoginAndPassword() = Pair(
        preferences.getString(LOGIN_STRING, null) ?: throwWasNotSavedException(),
        preferences.getString(PASSWORD_STRING, null) ?: throwWasNotSavedException()
    )

    private fun throwWasNotSavedException(): Nothing {
        throw RuntimeException("Values was not saved")
    }

    companion object {
        private const val PREFERENCES = "auth"

        private const val KEY_STRING = "auth_key"
        private const val CREATED_STRING = "auth_created"
        private const val ID_LONG = "auth_id"

        private const val LOGIN_STRING = "login"
        private const val PASSWORD_STRING = "password"
    }
}