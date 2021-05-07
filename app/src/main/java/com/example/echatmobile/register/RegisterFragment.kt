package com.example.echatmobile.register

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.echatmobile.R
import com.example.echatmobile.databinding.RegisterFragmentBinding
import com.example.echatmobile.di.modules.EchatViewModelFactoryModule
import com.example.echatmobile.login.ChangeAuthorizationButtonEvent
import com.example.echatmobile.system.BaseEventTypeInterface
import com.example.echatmobile.system.BaseFragment
import com.example.echatmobile.system.EchatApplication

class RegisterFragment : BaseFragment<RegisterViewModel, RegisterFragmentBinding>(),
    View.OnFocusChangeListener {
    override fun viewModel(): Class<RegisterViewModel> = RegisterViewModel::class.java
    override fun layoutId(): Int = R.layout.register_fragment
    override fun viewModelFactory(): ViewModelProvider.AndroidViewModelFactory =
        EchatApplication.instance
            .daggerApplicationComponent
            .plus(EchatViewModelFactoryModule())
            .getRegisterViewModelFactory()

    override fun handleExtendedObservers(baseEvent: BaseEventTypeInterface) {
        when (baseEvent) {
            is ChangeAuthorizationButtonEvent -> changeRegisterButton(
                baseEvent.color,
                baseEvent.clickable
            )
            else -> throw RuntimeException("Event doesn't supported")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
    }

    private fun initListeners() {
        binding.registerEditTextPassword.onFocusChangeListener = this
        binding.registerEditTextUsername.onFocusChangeListener = this
        binding.registerButton.setOnClickListener {
            viewModel.onRegisterButtonClick(
                binding.registerEditTextUsername.text.toString(),
                binding.registerEditTextPassword.text.toString()
            )
        }
    }

    private fun changeRegisterButton(color: Int, clickable: Boolean) {
        binding.registerButton.isEnabled = clickable
        binding.registerButton.setBackgroundColor(color)
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        viewModel.baseInputFieldRefocused(hasFocus)
    }
}