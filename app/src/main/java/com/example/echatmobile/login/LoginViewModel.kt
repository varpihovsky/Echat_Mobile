package com.example.echatmobile.login

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.viewModelScope
import com.example.echatmobile.R
import com.example.echatmobile.model.EchatModel
import com.example.echatmobile.profile.ProfileFragment.Companion.PROFILE_ID_KEY
import com.example.echatmobile.system.components.ui.architecture.AuthorizationViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    application: Application,
    private val echatModel: EchatModel
) : AuthorizationViewModel(application) {
    fun onRegisterButtonClick() {
        navigate(R.id.action_loginFragment_to_registerFragment)
    }

    fun onLoginButtonClick(login: String, password: String) {
        changeButtonState()
        GlobalScope.launch(Dispatchers.IO) {
            handleIO { handleLogin(login, password) }
            viewModelScope.launch { changeButtonState() }
        }
    }

    private fun handleLogin(login: String, password: String) {
            echatModel.authorize(login, password)
            echatModel.getCurrentUserProfile()?.id?.let {
                viewModelScope.launch {
                    navigate(R.id.action_loginFragment_to_profileFragment, Bundle().apply {
                        putLong(PROFILE_ID_KEY, it)
                    })
                }
            }
    }

    fun onFragmentCreated() {
        GlobalScope.launch(Dispatchers.IO) {
            if (echatModel.isLoginPresent()) {
                navigate(R.id.action_loginFragment_to_profileFragment)
            }
        }
    }
}