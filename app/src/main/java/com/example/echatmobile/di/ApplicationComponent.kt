package com.example.echatmobile.di

import android.app.Application
import android.content.Context
import com.example.echatmobile.di.modules.ApplicationModule
import com.example.echatmobile.di.modules.EchatViewModelFactoryModule
import com.example.echatmobile.di.modules.MainActivityModule
import com.example.echatmobile.di.scopes.ApplicationScope
import dagger.Component

@ApplicationScope
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {
    fun getApplication(): Application

    fun getContext(): Context

    fun plus(mainActivityModule: MainActivityModule): MainActivityComponent

    fun plus(echatViewModelFactoryModule: EchatViewModelFactoryModule): EchatViewModelFactoryComponent
}