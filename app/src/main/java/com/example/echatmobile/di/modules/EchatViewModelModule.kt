package com.example.echatmobile.di.modules

import android.app.Application
import com.example.echatmobile.MainActivityViewModel
import com.example.echatmobile.chat.ChatViewModel
import com.example.echatmobile.di.scopes.ViewModelScope
import com.example.echatmobile.invite.InviteViewModel
import com.example.echatmobile.login.LoginViewModel
import com.example.echatmobile.model.EchatModel
import com.example.echatmobile.new_chat.NewChatViewModel
import com.example.echatmobile.profile.ProfileViewModel
import com.example.echatmobile.profile.invites.ProfileInvitesViewModel
import com.example.echatmobile.profile.rooms.ProfileRoomsViewModel
import com.example.echatmobile.register.RegisterViewModel
import dagger.Module
import dagger.Provides

@Module
class EchatViewModelModule {
    @ViewModelScope
    @Provides
    fun getMainActivityViewModel(application: Application, echatModel: EchatModel) =
        MainActivityViewModel(echatModel, application)

    @ViewModelScope
    @Provides
    fun getLoginViewModel(application: Application, echatModel: EchatModel) =
        LoginViewModel(application, echatModel)

    @ViewModelScope
    @Provides
    fun getRegisterViewModel(application: Application, echatModel: EchatModel) =
        RegisterViewModel(application, echatModel)

    @ViewModelScope
    @Provides
    fun getProfileViewModel(application: Application, echatModel: EchatModel) =
        ProfileViewModel(application, echatModel)

    @ViewModelScope
    @Provides
    fun getNewChatViewModel(application: Application, echatModel: EchatModel) =
        NewChatViewModel(application, echatModel)

    @ViewModelScope
    @Provides
    fun getChatViewModel(application: Application, echatModel: EchatModel) =
        ChatViewModel(application, echatModel)

    @ViewModelScope
    @Provides
    fun getInviteViewModel(application: Application, echatModel: EchatModel) =
        InviteViewModel(application, echatModel)

    @ViewModelScope
    @Provides
    fun getProfileInvitesViewModel(application: Application, echatModel: EchatModel) =
        ProfileInvitesViewModel(application, echatModel)

    @ViewModelScope
    @Provides
    fun getProfileRoomsViewModel(application: Application, echatModel: EchatModel) =
        ProfileRoomsViewModel(application, echatModel)
}