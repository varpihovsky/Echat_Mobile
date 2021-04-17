package com.example.echatmobile.di

import com.example.echatmobile.di.modules.EchatModelModule
import com.example.echatmobile.di.modules.EchatViewModelModule
import com.example.echatmobile.di.scopes.ViewModelScope
import com.example.echatmobile.login.LoginViewModel
import com.example.echatmobile.register.RegisterViewModel
import dagger.Component
import dagger.Subcomponent

@ViewModelScope
@Component(
    dependencies = [ApplicationComponent::class, EchatModelComponent::class],
    modules = [EchatViewModelModule::class]
)
interface EchatViewModelComponent {
    fun provideLoginViewModel(): LoginViewModel

    fun provideRegisterViewModel(): RegisterViewModel
}