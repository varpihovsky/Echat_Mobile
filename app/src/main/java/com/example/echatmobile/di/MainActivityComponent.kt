package com.example.echatmobile.di

import com.example.echatmobile.di.modules.BaseFragmentModule
import com.example.echatmobile.di.modules.MainActivityModule
import dagger.Subcomponent

@Subcomponent(modules = [MainActivityModule::class])
interface MainActivityComponent {
    fun plus(baseFragmentModule: BaseFragmentModule): BaseFragmentComponent
}