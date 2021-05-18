package com.example.echatmobile.new_chat

import android.app.Application
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.echatmobile.R
import com.example.echatmobile.model.EchatModel
import com.example.echatmobile.model.entities.ChatDTO
import com.example.echatmobile.model.entities.UserWithoutPassword
import com.example.echatmobile.profile.ProfileFragment
import com.example.echatmobile.system.BaseEvent
import com.example.echatmobile.system.BaseFragment.Companion.TOAST_SHORT
import com.example.echatmobile.system.components.ListableViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewChatViewModel @Inject constructor(
    application: Application,
    private val echatModel: EchatModel
) : ListableViewModel<Any>(application) {
    private var searchType: String = ROOMS_SEARCH_TYPE

    fun onCreateButtonClick(roomName: String) {
        if (roomName.isEmpty()) {
            makeToast("Cant create room with empty name", Toast.LENGTH_SHORT)
            return
        }

        GlobalScope.launch(Dispatchers.IO) { handleIO { handleCreation(roomName) } }
    }

    private fun handleCreation(roomName: String) {
        echatModel.createChat(roomName)?.let {
            makeToast("Chat created", Toast.LENGTH_SHORT)
            return
        }
        makeToast("Chat wasn't created, please try another name", Toast.LENGTH_SHORT)
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
            .let { addAllToList(it) }
    }

    private fun searchForUsers(searchQuery: String) {
        addAllToList(echatModel.getUserProfileByQuery(searchQuery))
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

    fun onChatButtonClick(chatDTO: ChatDTO) {
        GlobalScope.launch(Dispatchers.IO) { handleIO { processChatJoining(chatDTO) } }
    }

    private fun processChatJoining(chatDTO: ChatDTO) {
        echatModel.joinToChat(chatDTO.id)
        removeFromList(chatDTO)
        makeToast("Joined to chat '${chatDTO.name}'", TOAST_SHORT)
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