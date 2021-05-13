package com.example.echatmobile.di

import com.example.echatmobile.MainActivityViewModel
import com.example.echatmobile.chat.ChatViewModel
import com.example.echatmobile.di.modules.EchatViewModelModule
import com.example.echatmobile.di.scopes.ViewModelScope
import com.example.echatmobile.invite.InviteViewModel
import com.example.echatmobile.login.LoginViewModel
import com.example.echatmobile.new_chat.NewChatViewModel
import com.example.echatmobile.profile.ProfileViewModel
import com.example.echatmobile.profile.invites.ProfileInvitesViewModel
import com.example.echatmobile.profile.rooms.ProfileRoomsViewModel
import com.example.echatmobile.register.RegisterViewModel
import dagger.Component

@ViewModelScope
@Component(
    dependencies = [ApplicationComponent::class, EchatModelComponent::class],
    modules = [EchatViewModelModule::class]
)
interface EchatViewModelComponent {
    fun provideMainActivityViewModel(): MainActivityViewModel

    fun provideLoginViewModel(): LoginViewModel

    fun provideRegisterViewModel(): RegisterViewModel

    fun provideProfileViewModel(): ProfileViewModel

    fun provideNewChatViewModel(): NewChatViewModel

    fun provideChatViewModel(): ChatViewModel

    fun provideInviteViewModel(): InviteViewModel

    fun provideProfileInvitesViewModel(): ProfileInvitesViewModel

    fun provideProfileRoomsViewModel(): ProfileRoomsViewModel
}