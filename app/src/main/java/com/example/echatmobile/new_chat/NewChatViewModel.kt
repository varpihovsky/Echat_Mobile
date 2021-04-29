package com.example.echatmobile.new_chat

import android.app.Application
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.echatmobile.R
import com.example.echatmobile.model.EchatModel
import com.example.echatmobile.model.entities.Chat
import com.example.echatmobile.model.entities.UserWithoutPassword
import com.example.echatmobile.profile.ProfileFragment
import com.example.echatmobile.system.BaseEvent
import com.example.echatmobile.system.BaseFragment.Companion.TOAST_SHORT
import com.example.echatmobile.system.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewChatViewModel @Inject constructor(
    application: Application,
    private val echatModel: EchatModel
) : BaseViewModel(application) {
    val data by lazy { Data() }

    private var searchType: String = ROOMS_SEARCH_TYPE
    private val dataList = MutableLiveData<List<Any>>()

    inner class Data {
        val dataList: LiveData<List<Any>> = this@NewChatViewModel.dataList
    }

    fun onCreateButtonClick(roomName: String) {
        if (roomName.isEmpty()) {
            makeToast("Cant create room with empty name", Toast.LENGTH_SHORT)
            return
        }

        GlobalScope.launch(Dispatchers.IO) { handleCreation(roomName) }
    }

    private fun handleCreation(roomName: String) {
        try {
            echatModel.createChat(roomName)?.let {
                makeToast("Chat created", Toast.LENGTH_SHORT)
                return
            }
            makeToast("Chat wasn't created, please try another name", Toast.LENGTH_SHORT)
        } catch (e: Exception) {
            e.message?.let { makeToast(it, Toast.LENGTH_SHORT) }
        }
    }

    fun onSearchButtonClick(searchQuery: String) {
        if (searchQuery.isEmpty()) {
            makeToast("Search field can not be empty", TOAST_SHORT)
            return
        }

        when (searchType) {
            ROOMS_SEARCH_TYPE -> GlobalScope.launch(Dispatchers.IO) { searchForRooms(searchQuery) }
            USERS_SEARCH_TYPE -> GlobalScope.launch(Dispatchers.IO) { searchForUsers(searchQuery) }
        }
    }

    private fun searchForRooms(searchQuery: String) {
        val currentUserChatList = echatModel.getCurrentUserChatList()
        echatModel.getChatsByQuery(searchQuery)
            .filter { currentUserChatList?.contains(it) == false }
            .let { viewModelScope.launch { dataList.value = it } }
    }

    private fun searchForUsers(searchQuery: String) {
        echatModel.getUserProfileByQuery(searchQuery).let {
            viewModelScope.launch { dataList.value = it }
        }
    }

    fun setUsersSearchTypeSelected() {
        searchType = USERS_SEARCH_TYPE
        baseEventLiveData.value =
            BaseEvent(ViewVisibilityEvent(NewChatFragment.CREATE_BUTTON, View.GONE))
    }

    fun setRoomsSearchTypeSelected() {
        searchType = ROOMS_SEARCH_TYPE
        baseEventLiveData.value =
            BaseEvent(ViewVisibilityEvent(NewChatFragment.CREATE_BUTTON, View.VISIBLE))
    }

    fun onChatButtonClick(chat: Chat) {
        GlobalScope.launch(Dispatchers.IO) { processChatJoining(chat) }
    }

    private fun processChatJoining(chat: Chat) {
        try {
            echatModel.joinToChat(chat.id)
            viewModelScope.launch {
                dataList.value?.indexOf(chat)?.let {
                    baseEventLiveData.value = BaseEvent(RemoveDataListItemEvent(it))

                    //TODO: fix bug: item doesn't removed
                    dataList.value = dataList.value?.toMutableList()?.apply { removeAt(it) }
                }
            }
            makeToast("Joined to chat '${chat.name}'", TOAST_SHORT)
        } catch (e: Exception) {
            e.message?.let { makeToast(it, TOAST_SHORT) }
        }
    }

    fun onUserButtonClick(user: UserWithoutPassword) {
        navigate(
            R.id.action_newChatFragment_to_profileFragment,
            Bundle().apply { putLong(ProfileFragment.PROFILE_ID_KEY, user.id) }
        )
    }

    companion object {
        private const val USERS_SEARCH_TYPE = "users"
        private const val ROOMS_SEARCH_TYPE = "rooms"
    }
}