package com.example.echatmobile.profile

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.echatmobile.R
import com.example.echatmobile.model.EchatModel
import com.example.echatmobile.model.entities.UserWithoutPassword
import com.example.echatmobile.system.BaseEvent
import com.example.echatmobile.system.BaseViewModel
import com.example.echatmobile.system.components.events.HideUnnecessaryDataEvent
import com.example.echatmobile.system.components.events.NavigateEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    application: Application,
    private val echatModel: EchatModel
) :
    BaseViewModel(application) {
    val data by lazy { Data() }
    private val user = MutableLiveData<UserWithoutPassword>()
    private var isUnnecessaryDataShown = true
    private val callbacks = mutableListOf<(Long) -> Unit>()

    inner class Data {
        val user: LiveData<UserWithoutPassword> = this@ProfileViewModel.user
    }

    fun loadProfileData(profileId: Long?) {
        GlobalScope.launch(Dispatchers.IO) {
            if (profileId == null || profileId == echatModel.getCurrentUserProfile()?.id) {
                handleCurrentProfile()
            } else {
                isUnnecessaryDataShown = false
                handleProfile(profileId)
            }
        }
    }

    private fun handleCurrentProfile() {
        echatModel.getCurrentUserProfile()?.let {
            UserWithoutPassword(it.id, it.login)
        }?.let {
            viewModelScope.launch {
                user.value = it
                executeCallbacks()
            }
        }
    }

    private fun handleProfile(profileId: Long) {
        viewModelScope.launch {
            baseEventLiveData.value = BaseEvent(HideUnnecessaryDataEvent())
        }
        echatModel.getUserProfileById(profileId)?.let {
            viewModelScope.launch {
                user.value = it
                executeCallbacks()
            }
        }
    }

    fun onNewChatButtonClick() {
        navigate(R.id.action_profileFragment_to_newChatFragment)
    }

    fun onInvitesClick() {
        baseEventLiveData.value =
            BaseEvent(NavigateEvent(R.id.action_profileRoomsFragment_to_profileInvitesFragment))
    }

    fun onChatsClick() {
        baseEventLiveData.value =
            BaseEvent(NavigateEvent(R.id.action_profileInvitesFragment_to_profileRoomsFragment))
    }

    fun addOnUserLoadCallback(callback: (Long) -> Unit) {
        if (user.value?.id != null) {
            user.value?.let { callback(it.id) }
            return
        }
        callbacks.add(callback)
    }

    private fun executeCallbacks() {
        callbacks.forEach { user.value?.id?.let(it) }
        callbacks.clear()
    }

    fun isUnnecessaryDataShown() = isUnnecessaryDataShown
}