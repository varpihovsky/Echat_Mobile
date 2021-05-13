package com.example.echatmobile

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.echatmobile.di.DaggerEchatViewModelComponent
import com.example.echatmobile.di.modules.EchatViewModelModule
import com.example.echatmobile.system.EchatApplication
import javax.inject.Inject

class MainActivityViewModelFactory @Inject constructor(application: Application) :
    ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        DaggerEchatViewModelComponent.builder()
            .echatViewModelModule(EchatViewModelModule())
            .echatModelComponent(EchatApplication.instance.daggerEchatModelComponent)
            .applicationComponent(EchatApplication.instance.daggerApplicationComponent)
            .build().provideMainActivityViewModel() as T
}