package com.example.echatmobile.di

import android.app.Application
import com.example.echatmobile.di.modules.*
import com.example.echatmobile.di.scopes.ApplicationScope
import dagger.Component

@ApplicationScope
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {
    fun getApplication(): Application
    
    fun plus(mainActivityModule: MainActivityModule): MainActivityComponent

    fun plus(echatViewModelFactoryModule: EchatViewModelFactoryModule): EchatViewModelFactoryComponent
}