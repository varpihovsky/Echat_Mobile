package com.example.echatmobile.login

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.echatmobile.di.modules.EchatModelModule
import com.example.echatmobile.di.modules.EchatViewModelModule
import com.example.echatmobile.system.EchatApplication

class LoginViewModelFactory(application: Application): ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        EchatApplication.instance
            .daggerApplicationComponent
            .plus(EchatModelModule(), EchatViewModelModule())
            .provideLoginViewModel() as T

}