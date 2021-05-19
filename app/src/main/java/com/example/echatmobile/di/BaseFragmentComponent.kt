package com.example.echatmobile.di

import androidx.databinding.ViewDataBinding
import com.example.echatmobile.di.modules.BaseFragmentModule
import com.example.echatmobile.di.scopes.ActivityScope
import com.example.echatmobile.system.BaseFragment
import com.example.echatmobile.system.BaseViewModel
import com.example.echatmobile.system.components.ui.architecture.ComposableBaseFragment
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [BaseFragmentModule::class])
interface BaseFragmentComponent {
    fun inject(baseFragment: BaseFragment<BaseViewModel, ViewDataBinding>)

    fun inject(baseFragment: ComposableBaseFragment<BaseViewModel>)
}