package com.example.echatmobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Debug
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.echatmobile.databinding.ActivityMainBinding
import com.example.echatmobile.di.BaseFragmentComponent
import com.example.echatmobile.di.modules.BaseFragmentModule
import com.example.echatmobile.di.scopes.ActivityScope
import com.example.echatmobile.system.EchatApplication

@ActivityScope
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }
}