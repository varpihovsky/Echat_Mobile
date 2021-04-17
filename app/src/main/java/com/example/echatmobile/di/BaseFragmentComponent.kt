package com.example.echatmobile.di

import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.example.echatmobile.di.modules.ApplicationModule
import com.example.echatmobile.di.modules.BaseFragmentModule
import com.example.echatmobile.di.scopes.ActivityScope
import com.example.echatmobile.di.scopes.ApplicationScope
import com.example.echatmobile.login.LoginViewModel
import com.example.echatmobile.system.BaseFragment
import com.example.echatmobile.system.BaseViewModel
import dagger.Component
import dagger.Subcomponent
import javax.inject.Singleton

@ActivityScope
@Subcomponent(modules = [BaseFragmentModule::class])
interface BaseFragmentComponent {
    fun inject(baseFragment: BaseFragment<BaseViewModel, ViewDataBinding>)
}