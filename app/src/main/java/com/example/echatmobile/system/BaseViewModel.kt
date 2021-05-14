package com.example.echatmobile.system

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.echatmobile.system.BaseFragment.Companion.TOAST_SHORT
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
            baseEventLiveData.value = BaseEvent(ToastStringEvent(text, toastLength))
        }
    }


    protected fun makeToast(resource: Int, toastLength: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            baseEventLiveData.value =
                BaseEvent(ToastResourceEvent(resource, toastLength))
        }
    }

    protected fun hideKeyboard() {
        baseEventLiveData.value = BaseEvent(HideKeyboardEvent())
    }

    fun baseInputFieldRefocused(focused: Boolean) {
        if (!focused) {
            hideKeyboard()
        }
    }

    protected fun navigate(action: Int, data: Bundle? = null) {
        GlobalScope.launch(Dispatchers.Main) {
            baseEventLiveData.value = BaseEvent(NavigateEvent(action, data))
        }
    }

    protected fun handleIO(block: () -> Unit) {
        try {
            block()
        } catch (e: Exception) {
            e.message?.let { makeToast(it, TOAST_SHORT) }
        }
    }
}