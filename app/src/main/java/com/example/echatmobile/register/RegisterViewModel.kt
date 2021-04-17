package com.example.echatmobile.register

import android.app.Application
import android.graphics.Color
import android.widget.Toast
import com.example.echatmobile.R
import com.example.echatmobile.login.AuthorizationEvents
import com.example.echatmobile.login.ChangeAuthorizationButtonEventBuilder
import com.example.echatmobile.model.EchatModel
import com.example.echatmobile.system.BaseEvent
import com.example.echatmobile.system.BaseFragment.Companion.TOAST_LONG
import com.example.echatmobile.system.BaseFragment.Companion.TOAST_SHORT
import com.example.echatmobile.system.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class RegisterViewModel @Inject constructor(
    application: Application,
    private val echatModel: EchatModel
) :
    BaseViewModel(application) {
    fun onBackButtonClick() {
        navigate(R.id.action_registerFragment_to_loginFragment)
    }

    fun onRegisterButtonClick(login: String, password: String) {
        setRegisterButtonNotClickable()
        GlobalScope.launch(Dispatchers.IO) { handleRegister(login, password) }
    }

    private fun handleRegister(login: String, password: String) {
        try {
            echatModel.register(login, password)
            GlobalScope.launch(Dispatchers.Main) {
                makeToast("User successfully registered! You can login now", TOAST_SHORT)
            }
        } catch (e: Exception) {
            GlobalScope.launch(Dispatchers.Main) { e.message?.let { makeToast(it, TOAST_LONG) } }
        }
        GlobalScope.launch(Dispatchers.Main) { setRegisterButtonClickable() }
    }

    private fun setRegisterButtonNotClickable() {
        baseEventLiveData.value =
            BaseEvent(
                AuthorizationEvents.CHANGE_AUTHORIZATION_BUTTON
                    .builder<ChangeAuthorizationButtonEventBuilder>()
                    .setClickable(false)
                    .setColor(Color.GRAY)
                    .build()
            )
    }

    private fun setRegisterButtonClickable() {
        baseEventLiveData.value =
            BaseEvent(
                AuthorizationEvents.CHANGE_AUTHORIZATION_BUTTON
                    .builder<ChangeAuthorizationButtonEventBuilder>()
                    .setClickable(true)
                    .setColor(Color.parseColor("#6200EE"))
                    .build()
            )
    }
}