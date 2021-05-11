package com.example.echatmobile.di.modules

import androidx.navigation.Navigation
import com.example.echatmobile.MainActivity
import com.example.echatmobile.R
import dagger.Module
import dagger.Provides

@Module
class MainActivityModule(private val mainActivity: MainActivity) {
    @Provides
    fun getNavController() = Navigation.findNavController(mainActivity, R.id.navigation_fragment)
}