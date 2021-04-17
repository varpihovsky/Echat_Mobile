package com.example.echatmobile.di.modules

import android.app.Application
import com.example.echatmobile.di.scopes.ViewModelScope
import com.example.echatmobile.login.LoginViewModel
import com.example.echatmobile.model.EchatModel
import com.example.echatmobile.profile.ProfileViewModel
import com.example.echatmobile.register.RegisterViewModel
import dagger.Module
import dagger.Provides

@Module
class EchatViewModelModule {
    @ViewModelScope
    @Provides
    fun getLoginViewModel(application: Application, echatModel: EchatModel) = LoginViewModel(application, echatModel)

    @ViewModelScope
    @Provides
    fun getRegisterViewModel(application: Application, echatModel: EchatModel) = RegisterViewModel(application, echatModel)

    @ViewModelScope
    @Provides
    fun getProfileViewModel(application: Application, echatModel: EchatModel) = ProfileViewModel(application, echatModel)
}