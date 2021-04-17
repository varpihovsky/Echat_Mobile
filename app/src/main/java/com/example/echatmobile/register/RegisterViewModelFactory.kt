package com.example.echatmobile.register

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.echatmobile.di.DaggerEchatViewModelComponent
import com.example.echatmobile.di.modules.EchatModelModule
import com.example.echatmobile.di.modules.EchatViewModelFactoryModule
import com.example.echatmobile.di.modules.EchatViewModelModule
import com.example.echatmobile.system.EchatApplication
import javax.inject.Inject

class RegisterViewModelFactory @Inject constructor(application: Application) :
    ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        DaggerEchatViewModelComponent.builder()
            .echatModelComponent(EchatApplication.instance.daggerEchatModelComponent)
            .echatViewModelModule(EchatViewModelModule())
            .applicationComponent(EchatApplication.instance.daggerApplicationComponent)
            .build()
            .provideRegisterViewModel() as T

}