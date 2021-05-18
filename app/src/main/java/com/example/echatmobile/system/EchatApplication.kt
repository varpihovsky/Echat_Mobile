package com.example.echatmobile.system

import android.app.Application
import com.example.echatmobile.di.ApplicationComponent
import com.example.echatmobile.di.DaggerApplicationComponent
import com.example.echatmobile.di.DaggerEchatModelComponent
import com.example.echatmobile.di.EchatModelComponent
import com.example.echatmobile.di.modules.ApplicationModule
import com.example.echatmobile.di.modules.EchatModelModule
import com.example.echatmobile.di.scopes.ApplicationScope

@ApplicationScope
class EchatApplication : Application() {
    lateinit var daggerApplicationComponent: ApplicationComponent
    lateinit var daggerEchatModelComponent: EchatModelComponent

    override fun onCreate() {
        super.onCreate()

        initApplicationComponent()
        initModelComponent()
        instance = this
    }

    private fun initApplicationComponent(){
        daggerApplicationComponent =
            DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
    }

    private fun initModelComponent(){
        daggerEchatModelComponent =
            DaggerEchatModelComponent.builder()
                .echatModelModule(EchatModelModule())
                .applicationComponent(daggerApplicationComponent)
                .build()
    }

    companion object {
        lateinit var instance: EchatApplication

        const val LOG_TAG = "Application"
    }
}