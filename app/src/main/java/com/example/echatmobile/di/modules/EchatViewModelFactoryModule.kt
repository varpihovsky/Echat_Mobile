package com.example.echatmobile.di.modules

import android.app.Application
import com.example.echatmobile.chat.ChatViewModelFactory
import com.example.echatmobile.invite.InviteViewModelFactory
import com.example.echatmobile.login.LoginViewModelFactory
import com.example.echatmobile.new_chat.NewChatViewModelFactory
import com.example.echatmobile.profile.ProfileViewModelFactory
import com.example.echatmobile.profile.invites.ProfileInvitesViewModelFactory
import com.example.echatmobile.profile.rooms.ProfileRoomsViewModelFactory
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
    fun provideNewChatViewModelFactory(application: Application) =
        NewChatViewModelFactory(application)

    @Provides
    fun provideChatViewModelFactory(application: Application) = ChatViewModelFactory(application)

    @Provides
    fun provideInviteViewModelFactory(application: Application) =
        InviteViewModelFactory(application)

    @Provides
    fun provideProfileInvitesViewModelFactory(application: Application) =
        ProfileInvitesViewModelFactory(application)

    @Provides
    fun provideProfileRoomsViewModelFactory(application: Application) =
        ProfileRoomsViewModelFactory(application)
}