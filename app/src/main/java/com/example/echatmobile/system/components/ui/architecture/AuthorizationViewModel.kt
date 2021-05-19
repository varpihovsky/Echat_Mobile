package com.example.echatmobile.system.components.ui.architecture

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.example.echatmobile.system.BaseEvent
import com.example.echatmobile.system.BaseViewModel
import com.example.echatmobile.system.components.events.ChangeAuthorizationButtonEvent
import kotlinx.coroutines.launch

abstract class AuthorizationViewModel(application: Application) : BaseViewModel(application) {
    protected fun changeButtonState() {
        viewModelScope.launch {
            baseEventLiveData.value = BaseEvent(ChangeAuthorizationButtonEvent())
        }
    }
}