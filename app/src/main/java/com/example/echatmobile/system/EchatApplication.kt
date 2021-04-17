package com.example.echatmobile.system

import android.app.Application
import android.os.Debug
import com.example.echatmobile.di.ApplicationComponent
import com.example.echatmobile.di.DaggerApplicationComponent
import com.example.echatmobile.di.modules.ApplicationModule
import com.example.echatmobile.di.scopes.ApplicationScope

@ApplicationScope
class EchatApplication : Application() {
    lateinit var daggerApplicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        initApplicationComponent()
        instance = this
    }

    private fun initApplicationComponent(){
        daggerApplicationComponent =
            DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
    }

    companion object {
        lateinit var instance: EchatApplication

        const val LOG_TAG = "Application"
    }
}