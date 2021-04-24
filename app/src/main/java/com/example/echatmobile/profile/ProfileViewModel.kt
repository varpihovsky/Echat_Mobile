package com.example.echatmobile.profile

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.echatmobile.R
import com.example.echatmobile.chat.ChatFragment
import com.example.echatmobile.model.EchatModel
import com.example.echatmobile.model.entities.Chat
import com.example.echatmobile.model.entities.UserWithoutPassword
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
    val user = MutableLiveData<UserWithoutPassword>()
    private val chatList = MutableLiveData<List<Chat>>()

    inner class Data {
        val chatList: LiveData<List<Chat>> = this@ProfileViewModel.chatList
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
        chatList.postValue(echatModel.getChatsByParticipantId(userWithoutPassword.id))
    }

    fun onNewChatButtonClick() {
        navigate(R.id.action_profileFragment_to_newChatFragment)
    }

    fun onChatItemClick(chat: Chat) {
        navigate(
            R.id.action_profileFragment_to_chatFragment,
            Bundle().apply { putLong(ChatFragment.CHAT_ID_ARGUMENT, chat.id) })
    }
}