package com.example.echatmobile.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.example.echatmobile.di.EchatViewModelFactoryComponent
import com.example.echatmobile.system.BaseEventTypeInterface
import com.example.echatmobile.system.components.ui.architecture.AuthorizationFragment
import com.example.echatmobile.system.components.ui.composables.Authorization

class LoginFragment : AuthorizationFragment<LoginViewModel>() {
    override fun viewModel(): Class<LoginViewModel> = LoginViewModel::class.java

    override fun viewModelFactorySelector(): (EchatViewModelFactoryComponent.() -> ViewModelProvider.AndroidViewModelFactory) =
        provideViewModelSelector { getLoginViewModelFactory() }

    override fun handleExtendedObservers(baseEvent: BaseEventTypeInterface) {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                mainButtonState = remember { mutableStateOf(true) }

                Authorization(
                    title = "Echat Mobile",
                    mainButtonText = "LOGIN",
                    centerTextButtonText = "Forgot password?",
                    onMainButtonClick = { viewModel.onLoginButtonClick(it.first, it.second) },
                    isMainButtonClickable = mainButtonState.value,
                    bottomTextButtonText = "Register",
                    onBottomTextButtonClick = { viewModel.onRegisterButtonClick() }
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onFragmentCreated()
    }
}

@Preview
@Composable
fun LoginPreview() {
    Authorization(
        title = "Echat Mobile",
        mainButtonText = "LOGIN",
        centerTextButtonText = "Forgot password?",
        onMainButtonClick = { TODO() },
        bottomTextButtonText = "Register"
    )
}