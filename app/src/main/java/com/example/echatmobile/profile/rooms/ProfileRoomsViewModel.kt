package com.example.echatmobile.profile.rooms

import android.app.Application
import android.os.Bundle
import com.example.echatmobile.R
import com.example.echatmobile.chat.ChatFragment
import com.example.echatmobile.invite.InviteFragment
import com.example.echatmobile.model.EchatModel
import com.example.echatmobile.model.entities.Chat
import com.example.echatmobile.system.components.ListableViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileRoomsViewModel @Inject constructor(
    application: Application,
    private val echatModel: EchatModel
) : ListableViewModel<Chat>(application) {

    fun onFragmentCreated(profileId: Long?) {
        if (isListEmpty() && profileId == null) {
            GlobalScope.launch(Dispatchers.IO) { handleIO { initCurrentUserDataList() } }
        }
        if (isListEmpty() && profileId != null) {
            GlobalScope.launch(Dispatchers.IO) { handleIO { initUserProfileList(profileId) } }
        }
    }

    private fun initCurrentUserDataList() {
        echatModel.getCurrentUserChatList()?.let { addAllToList(it) }
    }

    private fun initUserProfileList(profileId: Long) {
        addAllToList(echatModel.getChatsByParticipantId(profileId))
    }

    fun onItemClick(chat: Chat) {
        navigate(
            R.id.action_profileFragment_to_chatFragment,
            Bundle().apply { putLong(ChatFragment.CHAT_ID_ARGUMENT, chat.id) })
    }

    fun onItemRemoveClick(chat: Chat) {
        // Chat leaving function is not implemented on server
        removeFromList(chat)
    }

    fun onInviteClick(chat: Chat) {
        navigate(
            R.id.action_profileFragment_to_inviteFragment,
            Bundle().apply { putLong(InviteFragment.CHAT_ID_PARAM, chat.id) })
    }
}