package com.example.echatmobile.register

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

class RegisterFragment : AuthorizationFragment<RegisterViewModel>() {
    override fun viewModel(): Class<RegisterViewModel> = RegisterViewModel::class.java

    override fun viewModelFactorySelector(): (EchatViewModelFactoryComponent.() -> ViewModelProvider.AndroidViewModelFactory) =
        provideViewModelSelector { getRegisterViewModelFactory() }

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
                    title = "Register",
                    onMainButtonClick = { viewModel.onRegisterButtonClick(it.first, it.second) },
                    isMainButtonClickable = mainButtonState.value,
                    mainButtonText = "REGISTER"
                )
            }
        }
    }
}

@Preview
@Composable
fun RegisterPreview() {
    Authorization(
        title = "Register",
        onMainButtonClick = {},
        mainButtonText = "REGISTER"
    )
}