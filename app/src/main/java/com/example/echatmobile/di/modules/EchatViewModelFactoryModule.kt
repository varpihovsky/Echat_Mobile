package com.example.echatmobile.di.modules

import android.app.Application
import com.example.echatmobile.login.LoginViewModelFactory
import com.example.echatmobile.new_chat.NewChatViewModelFactory
import com.example.echatmobile.profile.ProfileViewModelFactory
import com.example.echatmobile.register.RegisterViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class EchatViewModelFactoryModule {
    @Provides
    fun provideLoginFactory(application: Application) = LoginViewModelFactory(application)

    @Provides
    fun provideRegisterFactory(application: Application) = RegisterViewModelFactory(application)

    @Provides
    fun provideProfileFactory(application: Application) = ProfileViewModelFactory(application)

    @Provides
    fun provideNewChatViewModelFactory(application: Application) = NewChatViewModelFactory(application)
}