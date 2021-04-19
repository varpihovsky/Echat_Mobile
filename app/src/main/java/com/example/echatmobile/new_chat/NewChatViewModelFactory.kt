package com.example.echatmobile.new_chat

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.echatmobile.di.DaggerEchatViewModelComponent
import com.example.echatmobile.di.modules.EchatViewModelModule
import com.example.echatmobile.system.EchatApplication
import javax.inject.Inject

class NewChatViewModelFactory @Inject constructor(application: Application) :
    ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        DaggerEchatViewModelComponent.builder()
            .applicationComponent(EchatApplication.instance.daggerApplicationComponent)
            .echatModelComponent(EchatApplication.instance.daggerEchatModelComponent)
            .echatViewModelModule(EchatViewModelModule())
            .build()
            .provideNewChatViewModel() as T

}