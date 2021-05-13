package com.example.echatmobile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.echatmobile.model.EchatModel
import com.example.echatmobile.system.BaseEvent
import com.example.echatmobile.system.NavigateEvent
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    private val echatModel: EchatModel,
    application: Application
) : AndroidViewModel(application) {
    val data by lazy { Data() }

    private val navigateEvent = MutableLiveData<BaseEvent<NavigateEvent>>()

    inner class Data {
        val navigateEvent: LiveData<BaseEvent<NavigateEvent>> =
            this@MainActivityViewModel.navigateEvent
    }

    fun onLogoutClick() {
        echatModel.logout()
        navigateEvent.value = BaseEvent(NavigateEvent(R.id.loginFragment, null))
    }
}