package com.example.echatmobile.di

import com.example.echatmobile.chat.ChatViewModelFactory
import com.example.echatmobile.di.modules.EchatViewModelFactoryModule
import com.example.echatmobile.login.LoginViewModelFactory
import com.example.echatmobile.new_chat.NewChatViewModelFactory
import com.example.echatmobile.profile.ProfileViewModelFactory
import com.example.echatmobile.register.RegisterViewModelFactory
import dagger.Subcomponent

@Subcomponent(modules = [EchatViewModelFactoryModule::class])
interface EchatViewModelFactoryComponent {
    fun getLoginViewModelFactory(): LoginViewModelFactory

    fun getRegisterViewModelFactory(): RegisterViewModelFactory

    fun getProfileViewModelFactory(): ProfileViewModelFactory

    fun getNewChatViewModelFactory(): NewChatViewModelFactory

    fun getChatViewModelFactory(): ChatViewModelFactory
}