package com.example.echatmobile.register

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.example.echatmobile.model.EchatModel
import com.example.echatmobile.system.BaseFragment.Companion.TOAST_SHORT
import com.example.echatmobile.system.components.ui.architecture.AuthorizationViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegisterViewModel @Inject constructor(
    application: Application,
    private val echatModel: EchatModel
) : AuthorizationViewModel(application) {
    fun onRegisterButtonClick(login: String, password: String) {
        changeButtonState()
        GlobalScope.launch(Dispatchers.IO) {
            handleIO { handleRegister(login, password) }
            viewModelScope.launch { changeButtonState() }
        }
    }

    private fun handleRegister(login: String, password: String) {
        echatModel.register(login, password)
        GlobalScope.launch(Dispatchers.Main) {
            makeToast("User successfully registered! You can login now", TOAST_SHORT)
        }
    }
}