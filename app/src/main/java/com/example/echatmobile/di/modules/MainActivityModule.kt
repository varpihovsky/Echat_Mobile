package com.example.echatmobile.di.modules

import android.view.View
import androidx.core.app.ActivityCompat
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.echatmobile.MainActivity
import com.example.echatmobile.R
import dagger.Module
import dagger.Provides

@Module
class MainActivityModule(private val mainActivity: MainActivity) {
    @Provides
    fun getNavController() = Navigation.findNavController(mainActivity, R.id.navigation_fragment)
}