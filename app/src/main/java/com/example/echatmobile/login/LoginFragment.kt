package com.example.echatmobile.login

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import com.example.echatmobile.R
import com.example.echatmobile.databinding.LoginFragmentBinding
import com.example.echatmobile.di.EchatViewModelFactoryComponent
import com.example.echatmobile.system.components.AuthorizationFragment

class LoginFragment : AuthorizationFragment<LoginViewModel, LoginFragmentBinding>() {
    override fun viewModel(): Class<LoginViewModel> = LoginViewModel::class.java

    override fun layoutId(): Int = R.layout.login_fragment

    override fun authorizationButton(): Button = binding.loginButton

    override fun viewModelFactorySelector(): (EchatViewModelFactoryComponent.() -> ViewModelProvider.AndroidViewModelFactory) =
        provideViewModelSelector { getLoginViewModelFactory() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBinding()
        viewModel.onFragmentCreated()
    }

    private fun initBinding() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }
}