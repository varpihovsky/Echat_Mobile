package com.example.echatmobile.login

import android.app.Application
import android.graphics.Color
import android.widget.Toast
import com.example.echatmobile.R
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

class LoginViewModel @Inject constructor(
    application: Application,
    private val echatModel: EchatModel
) : BaseViewModel(application) {
    fun onRegisterButtonClick() {
        navigate(R.id.action_loginFragment_to_registerFragment)
    }

    fun onLoginButtonClick(login: String, password: String) {
        setLoginButtonNotClickable()
        GlobalScope.launch(Dispatchers.IO) { handleLogin(login, password) }
    }

    private fun handleLogin(login: String, password: String) {
        try {
            echatModel.authorize(login, password)
            GlobalScope.launch(Dispatchers.Main) { makeToast("Login successful", TOAST_SHORT) }
        } catch (e: Exception) {
            GlobalScope.launch(Dispatchers.Main) { e.message?.let { makeToast(it, TOAST_LONG) } }
        }
        GlobalScope.launch(Dispatchers.Main) { setLoginButtonClickable() }
    }

    private fun setLoginButtonNotClickable() {
        baseEventLiveData.value =
            BaseEvent(
                AuthorizationEvents.CHANGE_AUTHORIZATION_BUTTON
                    .builder<ChangeAuthorizationButtonEventBuilder>()
                    .setClickable(false)
                    .setColor(Color.GRAY)
                    .build()
            )
    }

    private fun setLoginButtonClickable() {
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