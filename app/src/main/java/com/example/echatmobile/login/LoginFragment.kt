package com.example.echatmobile.login

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.echatmobile.R
import com.example.echatmobile.databinding.LoginFragmentBinding
import com.example.echatmobile.di.EchatViewModelFactoryComponent
import com.example.echatmobile.system.BaseEventTypeInterface
import com.example.echatmobile.system.BaseFragment

class LoginFragment : BaseFragment<LoginViewModel, LoginFragmentBinding>() {
    override fun viewModel(): Class<LoginViewModel> = LoginViewModel::class.java
    override fun layoutId(): Int = R.layout.login_fragment

    override fun viewModelFactorySelector(): (EchatViewModelFactoryComponent.() -> ViewModelProvider.AndroidViewModelFactory) =
        provideViewModelSelector { getLoginViewModelFactory() }

    override fun handleExtendedObservers(baseEvent: BaseEventTypeInterface) {
        when (baseEvent) {
            is ChangeAuthorizationButtonEvent -> changeLoginButton(
                baseEvent.color,
                baseEvent.clickable
            )
            else -> throw RuntimeException("Unknown event type")
        }
    }

    private fun changeLoginButton(color: Int, clickable: Boolean) {
        binding.loginButton.setBackgroundColor(color)
        binding.loginButton.isEnabled = clickable
    }

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