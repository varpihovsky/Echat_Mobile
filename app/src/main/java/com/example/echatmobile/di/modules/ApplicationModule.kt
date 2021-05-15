package com.example.echatmobile.di.modules

import android.app.Application
import android.content.Context
import com.example.echatmobile.di.scopes.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(private val application: Application) {
    @ApplicationScope
    @Provides
    fun provideApplication() = application

    @ApplicationScope
    @Provides
    fun provideContext() = application as Context
}