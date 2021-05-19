package com.example.echatmobile.profile.rooms

import android.app.Application
import android.os.Bundle
import com.example.echatmobile.R
import com.example.echatmobile.chat.ChatFragment
import com.example.echatmobile.invite.InviteFragment
import com.example.echatmobile.model.EchatModel
import com.example.echatmobile.model.entities.ChatDTO
import com.example.echatmobile.system.components.ui.architecture.ListableViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileRoomsViewModel @Inject constructor(
    application: Application,
    private val echatModel: EchatModel
) : ListableViewModel<ChatDTO>(application) {

    fun onFragmentCreated(profileId: Long?) {
        if (isListEmpty() && profileId == null) {
            GlobalScope.launch(Dispatchers.IO) { handleIO { initCurrentUserDataList() } }
        }
        if (isListEmpty() && profileId != null) {
            GlobalScope.launch(Dispatchers.IO) { handleIO { initUserProfileList(profileId) } }
        }
        if (!isListEmpty()) {
            GlobalScope.launch(Dispatchers.IO) { handleIO { updateList(profileId) } }
        }
    }

    private fun initCurrentUserDataList() {
        echatModel.getCurrentUserChatList()?.let { addAllToList(it) }
    }

    private fun initUserProfileList(profileId: Long) {
        addAllToList(echatModel.getChatsByParticipantId(profileId))
    }

    private fun updateList(profileId: Long?) {
        if (profileId != null) {
            echatModel.getChatsByParticipantId(profileId)
                .filter { listableData.dataList.value?.contains(it) == false }.forEach {
                    addToList(it)
                }
        } else {
            echatModel.getCurrentUserChatList()
                ?.filter { listableData.dataList.value?.contains(it) == false }?.forEach {
                    addToList(it)
                }
        }
    }

    fun onItemClick(chatDTO: ChatDTO) {
        navigate(
            R.id.action_profileFragment_to_chatFragment,
            Bundle().apply { putLong(ChatFragment.CHAT_ID_ARGUMENT, chatDTO.id) })
    }

    fun onItemRemoveClick(chatDTO: ChatDTO) {
        // Chat leaving function is not implemented on server
        removeFromList(chatDTO)
    }

    fun onInviteClick(chatDTO: ChatDTO) {
        navigate(
            R.id.action_profileFragment_to_inviteFragment,
            Bundle().apply { putLong(InviteFragment.CHAT_ID_PARAM, chatDTO.id) })
    }
}