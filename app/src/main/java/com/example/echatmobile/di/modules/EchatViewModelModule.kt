package com.example.echatmobile.di.modules

import android.app.Application
import com.example.echatmobile.login.LoginViewModel
import com.example.echatmobile.model.EchatModel
import com.example.echatmobile.register.RegisterViewModel
import dagger.Module
import dagger.Provides

@Module(includes = [EchatModelModule::class])
class EchatViewModelModule {
    @Provides
    fun getLoginViewModel(application: Application, echatModel: EchatModel) = LoginViewModel(application, echatModel)

    @Provides
    fun getRegisterViewModel(application: Application, echatModel: EchatModel) = RegisterViewModel(application, echatModel)
}