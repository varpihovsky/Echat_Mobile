package com.example.echatmobile.di

import com.example.echatmobile.chat.ChatViewModelFactory
import com.example.echatmobile.di.modules.EchatViewModelFactoryModule
import com.example.echatmobile.invite.InviteViewModelFactory
import com.example.echatmobile.login.LoginViewModelFactory
import com.example.echatmobile.new_chat.NewChatViewModelFactory
import com.example.echatmobile.profile.ProfileViewModelFactory
import com.example.echatmobile.profile.invites.ProfileInvitesViewModelFactory
import com.example.echatmobile.profile.rooms.ProfileRoomsViewModelFactory
import com.example.echatmobile.register.RegisterViewModelFactory
import dagger.Subcomponent

@Subcomponent(modules = [EchatViewModelFactoryModule::class])
interface EchatViewModelFactoryComponent {
    fun getLoginViewModelFactory(): LoginViewModelFactory

    fun getRegisterViewModelFactory(): RegisterViewModelFactory

    fun getProfileViewModelFactory(): ProfileViewModelFactory

    fun getNewChatViewModelFactory(): NewChatViewModelFactory

    fun getChatViewModelFactory(): ChatViewModelFactory

    fun getInviteViewModelFactory(): InviteViewModelFactory

    fun getProfileInvitesViewModelFactory(): ProfileInvitesViewModelFactory

    fun getProfileRoomsViewModelFactory(): ProfileRoomsViewModelFactory
}