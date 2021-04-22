package com.example.echatmobile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.echatmobile.databinding.ActivityMainBinding
import com.example.echatmobile.di.scopes.ActivityScope

@ActivityScope
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

//    override fun onBackPressed() {
//        super.onBackPressed()
//        Navigation.findNavController(this, R.id.navigation_fragment).navigateUp()
//    }
}