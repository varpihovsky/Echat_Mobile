package com.example.echatmobile.invite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.echatmobile.model.EchatModel
import com.example.echatmobile.model.entities.UserWithoutPassword
import com.example.echatmobile.new_chat.RemoveDataListItemEvent
import com.example.echatmobile.system.BaseEvent
import com.example.echatmobile.system.BaseFragment.Companion.TOAST_SHORT
import com.example.echatmobile.system.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class InviteViewModel @Inject constructor(
    application: Application,
    private val echatModel: EchatModel
) : BaseViewModel(application) {
    val data by lazy { Data() }

    private val dataList = MutableLiveData<List<UserWithoutPassword>>()
    private var chatId: Long = -1

    inner class Data {
        val dataList: LiveData<List<UserWithoutPassword>> = this@InviteViewModel.dataList
    }

    fun setChatId(chatId: Long) {
        this.chatId = chatId
    }

    fun onSearchButtonClick(username: String) {
        if (!isChatIdAssigned()) {
            return
        }

        GlobalScope.launch(Dispatchers.IO) { processSearch(username) }
    }

    private fun processSearch(username: String) {
        echatModel.getUserProfileByQuery(username).let {
            viewModelScope.launch { dataList.value = it }
        }
    }

    private fun isChatIdAssigned(): Boolean {
        if (chatId < 0) {
            makeToast("Unforeseen behaviour. Please try to reopen invite screen", TOAST_SHORT)
            return false
        }
        return true
    }

    fun onInviteButtonClick(user: UserWithoutPassword) {
        GlobalScope.launch(Dispatchers.IO) {
            echatModel.invite(chatId, user.id)
            dataList.value?.indexOf(user)?.let {
                viewModelScope.launch {
                    baseEventLiveData.value = BaseEvent(RemoveDataListItemEvent(it))
                    dataList.value = dataList.value?.toMutableList()?.apply { removeAt(it) }
                }
            }
        }
    }
}