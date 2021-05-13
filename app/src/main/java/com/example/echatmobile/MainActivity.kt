package com.example.echatmobile

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.echatmobile.databinding.ActivityMainBinding
import com.example.echatmobile.di.modules.EchatViewModelFactoryModule
import com.example.echatmobile.di.scopes.ActivityScope
import com.example.echatmobile.system.EchatApplication
import com.example.echatmobile.system.ServiceScheduler

@ActivityScope
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setSupportActionBar(binding.mainActivityToolbar)
        initNavController()
        initViewModel()
        initObservers()
        startServices()
    }

    private fun startMessageService() {
        ServiceScheduler(this).scheduleMessageService()
    }

    private fun initViewModel() {
        EchatApplication.instance.daggerApplicationComponent.plus(EchatViewModelFactoryModule())
            .getMainActivityViewModelFactory().let {
                viewModel = ViewModelProvider(this, it).get(MainActivityViewModel::class.java)
            }
    }

    private fun initNavController() {
        navController = Navigation.findNavController(this, R.id.navigation_fragment)
    }

    private fun initObservers() {
        viewModel.data.navigateEvent.observe(this) {
            it.get()?.let { navigateEvent -> processNavigate(navigateEvent.action) }
        }
    }

    private fun processNavigate(destination: Int) {
        navController.navigate(destination)
    }

    private fun startServices() {
        startMessageService()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.logout_action -> {
            viewModel.onLogoutClick()
            true
        }
        else -> false
    }
}