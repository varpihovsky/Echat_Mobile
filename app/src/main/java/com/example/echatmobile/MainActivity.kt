package com.example.echatmobile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.echatmobile.databinding.ActivityMainBinding
import com.example.echatmobile.di.scopes.ActivityScope
import com.example.echatmobile.system.ServiceScheduler

@ActivityScope
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        startServices()
    }

    private fun startServices() {
        startMessageService()
    }

    private fun startMessageService() {
        ServiceScheduler(this).scheduleMessageService()
    }
}