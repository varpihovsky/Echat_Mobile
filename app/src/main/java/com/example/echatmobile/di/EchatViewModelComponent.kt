package com.example.echatmobile.di

import com.example.echatmobile.di.modules.EchatModelModule
import com.example.echatmobile.di.modules.EchatViewModelModule
import com.example.echatmobile.login.LoginViewModel
import com.example.echatmobile.register.RegisterViewModel
import dagger.Subcomponent

@Subcomponent(modules = [EchatModelModule::class, EchatViewModelModule::class])
interface EchatViewModelComponent {
    fun provideLoginViewModel(): LoginViewModel

    fun provideRegisterViewModel(): RegisterViewModel
}