package com.example.echatmobile.system

import android.app.Application
import android.os.Bundle
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {
    val baseData: BaseData

    protected val baseEventLiveData = MutableLiveData<BaseEvent<BaseEventTypeInterface>>()

    private val callbacks = PropertyChangeRegistry()

    init{
        baseData = BaseData()
    }

    inner class BaseData {
        val baseEventLiveData: LiveData<BaseEvent<BaseEventTypeInterface>> =
            this@BaseViewModel.baseEventLiveData
    }

    protected fun makeToast(text: String, toastLength: Int) {
        baseEventLiveData.value =
            BaseEvent(
                BaseEventType.TOAST_STRING
                    .builder<ToastStringBuilder>()
                    .setText(text)
                    .setLength(toastLength).build()
            )
    }

    protected fun makeToast(resource: Int, toastLength: Int) {
        baseEventLiveData.value =
            BaseEvent(
                BaseEventType.TOAST_RESOURCE
                    .builder<ToastResourceBuilder>()
                    .setResource(resource)
                    .setLength(toastLength)
                    .build()
            )
    }

    protected fun hideKeyboard(){
        baseEventLiveData.value = BaseEvent(BaseEventType.HIDE_KEYBOARD)
    }

    fun baseInputFieldRefocused(focused: Boolean){
        if(!focused) {
            hideKeyboard()
        }
    }

    protected fun navigate(action: Int, data: Bundle? = null){
        baseEventLiveData.value =
            BaseEvent(
                BaseEventType.NAVIGATE
                    .builder<NavigateEventBuilder>()
                    .setAction(action)
                    .setNavigationData(data)
                    .build()
            )
    }
}