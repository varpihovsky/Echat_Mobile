package com.example.echatmobile.system.components

import android.app.Application
import android.graphics.Color
import com.example.echatmobile.system.BaseEvent
import com.example.echatmobile.system.BaseViewModel
import com.example.echatmobile.system.components.events.ChangeAuthorizationButtonEvent

abstract class AuthorizationViewModel(application: Application) : BaseViewModel(application) {
    protected fun setAuthorizationButtonClickable() {
        baseEventLiveData.value =
            BaseEvent(ChangeAuthorizationButtonEvent(Color.parseColor("#6200EE"), true))
    }

    protected fun setAuthorizationButtonNotClickable() {
        baseEventLiveData.value =
            BaseEvent(ChangeAuthorizationButtonEvent(Color.GRAY, false))
    }
}