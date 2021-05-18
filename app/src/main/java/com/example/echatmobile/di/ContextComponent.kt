package com.example.echatmobile.di

import android.content.Context
import com.example.echatmobile.di.modules.ContextModule
import dagger.Component

@Component(modules = [ContextModule::class])
interface ContextComponent {
    fun getContext(): Context
}