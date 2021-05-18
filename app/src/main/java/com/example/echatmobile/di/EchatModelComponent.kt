package com.example.echatmobile.di

import com.example.echatmobile.di.modules.EchatModelModule
import com.example.echatmobile.di.scopes.ModelScope
import com.example.echatmobile.model.EchatModel
import dagger.Component

@ModelScope
@Component(modules = [EchatModelModule::class], dependencies = [ApplicationComponent::class])
interface EchatModelComponent {
    fun provideModel(): EchatModel
}