package com.example.echatmobile.system

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {
    val baseData: BaseData

    protected val baseEventLiveData = MutableLiveData<BaseEvent<BaseEventTypeInterface>>()

    init {
        baseData = BaseData()
    }

    inner class BaseData {
        val baseEventLiveData: LiveData<BaseEvent<BaseEventTypeInterface>> =
            this@BaseViewModel.baseEventLiveData
    }

    protected fun makeToast(text: String, toastLength: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            baseEventLiveData.value =
                BaseEvent(
                    BaseEventType.TOAST_STRING
                        .builder<ToastStringBuilder>()
                        .setText(text)
                        .setLength(toastLength).build()
                )
            resetBaseEventLiveData()
        }
    }


    protected fun makeToast(resource: Int, toastLength: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            baseEventLiveData.value =
                BaseEvent(
                    BaseEventType.TOAST_RESOURCE
                        .builder<ToastResourceBuilder>()
                        .setResource(resource)
                        .setLength(toastLength)
                        .build()
                )
            resetBaseEventLiveData()
        }
    }

    protected fun hideKeyboard() {
        baseEventLiveData.value = BaseEvent(BaseEventType.HIDE_KEYBOARD)
        resetBaseEventLiveData()
    }

    fun baseInputFieldRefocused(focused: Boolean) {
        if (!focused) {
            hideKeyboard()
        }
    }

    protected fun navigate(action: Int, data: Bundle? = null) {
        GlobalScope.launch(Dispatchers.Main) {
            baseEventLiveData.value =
                BaseEvent(
                    BaseEventType.NAVIGATE
                        .builder<NavigateEventBuilder>()
                        .setAction(action)
                        .setNavigationData(data)
                        .build()
                )
            resetBaseEventLiveData()
        }
    }

    protected fun resetBaseEventLiveData() {
        baseEventLiveData.value = BaseEvent(BaseEventType.EMPTY)
    }
}