package com.example.echatmobile.login

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.echatmobile.R
import com.example.echatmobile.databinding.LoginFragmentBinding
import com.example.echatmobile.di.modules.EchatViewModelFactoryModule
import com.example.echatmobile.system.BaseEvent
import com.example.echatmobile.system.BaseEventTypeInterface
import com.example.echatmobile.system.BaseFragment
import com.example.echatmobile.system.EchatApplication
import java.lang.RuntimeException

class LoginFragment : BaseFragment<LoginViewModel, LoginFragmentBinding>(),
    View.OnFocusChangeListener {
    override fun viewModel(): Class<LoginViewModel> = LoginViewModel::class.java
    override fun layoutId(): Int = R.layout.login_fragment

    override fun viewModelFactory(): ViewModelProvider.AndroidViewModelFactory =
        EchatApplication.instance
            .daggerApplicationComponent
            .plus(EchatViewModelFactoryModule())
            .getLoginViewModelFactory()


    override fun handleExtendedObservers(baseEvent: BaseEvent<BaseEventTypeInterface>) {
        when(baseEvent.eventType){
            AuthorizationEvents.CHANGE_AUTHORIZATION_BUTTON -> changeLoginButton(baseEvent.eventType.data())
            else -> throw RuntimeException("Unknown event type")
        }
    }

    private fun changeLoginButton(authorizationButtonEventData: ChangeAuthorizationButtonEventData){
        binding.loginButton.setBackgroundColor(authorizationButtonEventData.color)
        binding.loginButton.isEnabled = authorizationButtonEventData.clickable
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
    }

    private fun initListeners() {
        binding.editTextUsername.onFocusChangeListener = this
        binding.editTextPassword.onFocusChangeListener = this
        binding.goToRegisterButton.setOnClickListener { viewModel.onRegisterButtonClick() }
        binding.loginButton.setOnClickListener {
            viewModel.onLoginButtonClick(
                binding.editTextUsername.text.toString(),
                binding.editTextPassword.text.toString()
            )
        }
    }

    override fun onFocusChange(v: View, hasFocus: Boolean) {
        viewModel.baseInputFieldRefocused(hasFocus)
    }
}