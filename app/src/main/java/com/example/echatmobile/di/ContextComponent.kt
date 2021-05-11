package com.example.echatmobile.di

import com.example.echatmobile.di.modules.ContextModule
import com.example.echatmobile.model.EchatModel
import dagger.Component

@Component(modules = [ContextModule::class])
interface ContextComponent {
    fun inject(echatModel: EchatModel)
}