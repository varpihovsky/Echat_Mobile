package com.example.echatmobile.system.components.ui.architecture

import androidx.compose.runtime.MutableState
import com.example.echatmobile.system.BaseEventTypeInterface
import com.example.echatmobile.system.BaseViewModel
import com.example.echatmobile.system.components.events.ChangeAuthorizationButtonEvent

abstract class AuthorizationFragment<T : BaseViewModel> :
    ComposableBaseFragment<T>() {
    protected lateinit var mainButtonState: MutableState<Boolean>

    override fun handleExtendedObservers(baseEvent: BaseEventTypeInterface) {
        when (baseEvent) {
            is ChangeAuthorizationButtonEvent -> changeButton()
            else -> throw RuntimeException("Unknown event type")
        }
    }

    private fun changeButton() {
        mainButtonState.value = !mainButtonState.value
    }
}