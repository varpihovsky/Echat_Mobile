package com.example.echatmobile.profile.invites

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.echatmobile.model.EchatModel
import com.example.echatmobile.model.entities.Invite
import com.example.echatmobile.new_chat.RemoveDataListItemEvent
import com.example.echatmobile.system.BaseEvent
import com.example.echatmobile.system.BaseFragment.Companion.TOAST_SHORT
import com.example.echatmobile.system.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileInvitesViewModel @Inject constructor(
    application: Application,
    private val echatModel: EchatModel
) : BaseViewModel(application) {
    val data by lazy { Data() }
    private val dataList = MutableLiveData<List<Invite>>()

    inner class Data {
        val dataList: LiveData<List<Invite>> = this@ProfileInvitesViewModel.dataList
    }

    fun onFragmentCreated() {
        if (dataList.value == null) {
            GlobalScope.launch(Dispatchers.IO) { initDataList() }
        }
    }

    private fun initDataList() {
        try {
            echatModel.getCurrentUserInvites().let {
                viewModelScope.launch { dataList.value = it }
            }
        } catch (e: Exception) {
            viewModelScope.launch { e.message?.let { makeToast(it, TOAST_SHORT) } }
        }
    }

    fun onInviteAccept(invite: Invite) {
        GlobalScope.launch(Dispatchers.IO) { processInvite(invite) { acceptInvite(invite.id) } }
    }

    fun onInviteDecline(invite: Invite) {
        GlobalScope.launch(Dispatchers.IO) { processInvite(invite) { declineInvite(invite.id) } }
    }

    private fun processInvite(invite: Invite, inviteMethod: EchatModel.() -> Unit) {
        try {
            inviteMethod(echatModel)
            viewModelScope.launch { removeInvite(invite) }
        } catch (e: Exception) {
            viewModelScope.launch { e.message?.let { makeToast(it, TOAST_SHORT) } }
        }
    }

    private fun removeInvite(invite: Invite) {
        dataList.value?.let {
            baseEventLiveData.value = BaseEvent(RemoveDataListItemEvent(it.indexOf(invite)))
            dataList.value = it.toMutableList().apply { remove(invite) }
        }
    }

}