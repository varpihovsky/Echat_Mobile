package com.example.echatmobile.register

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import com.example.echatmobile.R
import com.example.echatmobile.databinding.RegisterFragmentBinding
import com.example.echatmobile.di.EchatViewModelFactoryComponent
import com.example.echatmobile.system.components.AuthorizationFragment

class RegisterFragment : AuthorizationFragment<RegisterViewModel, RegisterFragmentBinding>(),
    View.OnFocusChangeListener {
    override fun viewModel(): Class<RegisterViewModel> = RegisterViewModel::class.java
    override fun layoutId(): Int = R.layout.register_fragment

    override fun authorizationButton(): Button = binding.registerButton

    override fun viewModelFactorySelector(): (EchatViewModelFactoryComponent.() -> ViewModelProvider.AndroidViewModelFactory) =
        provideViewModelSelector { getRegisterViewModelFactory() }

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

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        viewModel.baseInputFieldRefocused(hasFocus)
    }
}