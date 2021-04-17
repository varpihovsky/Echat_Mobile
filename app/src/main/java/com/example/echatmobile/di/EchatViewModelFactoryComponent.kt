package com.example.echatmobile.di

import com.example.echatmobile.di.modules.EchatViewModelFactoryModule
import com.example.echatmobile.login.LoginViewModelFactory
import com.example.echatmobile.profile.ProfileViewModelFactory
import com.example.echatmobile.register.RegisterViewModelFactory
import dagger.Subcomponent

@Subcomponent(modules = [EchatViewModelFactoryModule::class])
interface EchatViewModelFactoryComponent {
    fun getLoginViewModelFactory(): LoginViewModelFactory

    fun getRegisterViewModelFactory(): RegisterViewModelFactory

    fun getProfileViewModelFactory(): ProfileViewModelFactory
}