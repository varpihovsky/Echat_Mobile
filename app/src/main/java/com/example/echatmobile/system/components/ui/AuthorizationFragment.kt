package com.example.echatmobile.system.components

import android.widget.Button
import androidx.databinding.ViewDataBinding
import com.example.echatmobile.system.BaseEventTypeInterface
import com.example.echatmobile.system.BaseFragment
import com.example.echatmobile.system.BaseViewModel
import com.example.echatmobile.system.components.events.ChangeAuthorizationButtonEvent

abstract class AuthorizationFragment<T : BaseViewModel, B : ViewDataBinding> :
    BaseFragment<T, B>() {
    protected abstract fun authorizationButton(): Button

    override fun handleExtendedObservers(baseEvent: BaseEventTypeInterface) {
        when (baseEvent) {
            is ChangeAuthorizationButtonEvent -> changeButton(
                baseEvent.color,
                baseEvent.clickable
            )
            else -> throw RuntimeException("Unknown event type")
        }
    }

    private fun changeButton(color: Int, clickable: Boolean) {
        authorizationButton().setBackgroundColor(color)
        authorizationButton().isClickable = clickable
    }
}