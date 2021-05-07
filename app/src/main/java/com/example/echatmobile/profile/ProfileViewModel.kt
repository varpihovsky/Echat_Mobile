package com.example.echatmobile.profile

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.echatmobile.R
import com.example.echatmobile.model.EchatModel
import com.example.echatmobile.model.entities.UserWithoutPassword
import com.example.echatmobile.system.BaseEvent
import com.example.echatmobile.system.BaseViewModel
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


    inner class Data {
        val user: LiveData<UserWithoutPassword> = this@ProfileViewModel.user
    }

    fun loadProfileData(profileId: Long?) {
        GlobalScope.launch(Dispatchers.IO) {
            if (profileId == null) {
                handleCurrentProfile()
            } else {
                handleProfile(profileId)
            }
        }
    }

    private fun handleCurrentProfile() {
        echatModel.getCurrentUserProfile()?.let {
            UserWithoutPassword(it.id, it.login)
        }?.let { postData(it) }
    }

    private fun handleProfile(profileId: Long) {
        echatModel.getUserProfileById(profileId)?.let { postData(it) }
    }

    private fun postData(userWithoutPassword: UserWithoutPassword) {
        user.postValue(userWithoutPassword)
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
}