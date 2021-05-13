package com.example.echatmobile.profile.rooms

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.echatmobile.R
import com.example.echatmobile.chat.ChatFragment
import com.example.echatmobile.invite.InviteFragment
import com.example.echatmobile.model.EchatModel
import com.example.echatmobile.model.entities.Chat
import com.example.echatmobile.new_chat.RemoveDataListItemEvent
import com.example.echatmobile.system.BaseEvent
import com.example.echatmobile.system.BaseFragment.Companion.TOAST_SHORT
import com.example.echatmobile.system.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileRoomsViewModel @Inject constructor(
    application: Application,
    private val echatModel: EchatModel
) : BaseViewModel(application) {
    val data by lazy { Data() }
    private val dataList = MutableLiveData<List<Chat>>()

    inner class Data {
        val dataList: LiveData<List<Chat>> = this@ProfileRoomsViewModel.dataList
    }

    fun onFragmentCreated(profileId: Long?) {
        if (dataList.value == null && profileId == null) {
            GlobalScope.launch(Dispatchers.IO) { initCurrentUserDataList() }
        }
        if (dataList.value == null && profileId != null) {
            GlobalScope.launch { initUserProfileList(profileId) }
        }
    }

    private fun initCurrentUserDataList() {
        try {
            echatModel.getCurrentUserChatList()?.let {
                viewModelScope.launch { dataList.value = it }
            }
        } catch (e: Exception) {
            viewModelScope.launch { e.message?.let { makeToast(it, TOAST_SHORT) } }
        }
    }

    private fun initUserProfileList(profileId: Long) {
        try {
            echatModel.getChatsByParticipantId(profileId).let {
                viewModelScope.launch { dataList.value = it }
            }
        } catch (e: Exception) {
            viewModelScope.launch { e.message?.let { makeToast(it, TOAST_SHORT) } }
        }
    }

    fun onItemClick(chat: Chat) {
        navigate(
            R.id.action_profileFragment_to_chatFragment,
            Bundle().apply { putLong(ChatFragment.CHAT_ID_ARGUMENT, chat.id) })
    }

    fun onItemRemoveClick(chat: Chat) {
        // Chat leaving function is not implemented on server
        dataList.value?.let {
            baseEventLiveData.value = BaseEvent(RemoveDataListItemEvent(it.indexOf(chat)))
            dataList.value = it.toMutableList().apply { remove(chat) }
        }
    }

    fun onInviteClick(chat: Chat) {
        navigate(
            R.id.action_profileFragment_to_inviteFragment,
            Bundle().apply { putLong(InviteFragment.CHAT_ID_PARAM, chat.id) })
    }
}