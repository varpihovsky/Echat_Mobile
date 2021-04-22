package com.example.echatmobile.chat

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.echatmobile.model.EchatModel
import com.example.echatmobile.system.BaseEvent
import com.example.echatmobile.system.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.properties.Delegates

class ChatViewModel @Inject constructor(
    application: Application,
    private val echatModel: EchatModel
) : BaseViewModel(application) {

    val data by lazy { Data() }
    private val messagesLiveData = MutableLiveData<List<MessageDTO>>()
    private var chatId by Delegates.notNull<Long>()

    inner class Data {
        val messagesLiveData: LiveData<List<MessageDTO>> = this@ChatViewModel.messagesLiveData
    }

    fun loadChat(chatId: Long) {
        this.chatId = chatId
        GlobalScope.launch(Dispatchers.IO) { handleChatLoading(chatId) }
    }

    private fun handleChatLoading(chatId: Long) {
        try {
            val messagesDTO = retrieveMessagesList(chatId)
            GlobalScope.launch(Dispatchers.Main) { messagesLiveData.value = messagesDTO }
        } catch (e: Exception) {
            e.message?.let { makeToast(it, Toast.LENGTH_SHORT) }
        }
    }

    private fun retrieveMessagesList(chatId: Long) =
        echatModel.getMessageHistory(chatId).map {
            it.toDTO { message ->
                if (message.sender.login == echatModel.currentUserLogin) {
                    ALIGN_RIGHT
                } else {
                    ALIGN_LEFT
                }
            }
        }

    fun onSendButtonClick(text: String) {
        baseEventLiveData.value = BaseEvent(ClearChatFieldEvent())
        resetBaseEventLiveData()
        GlobalScope.launch(Dispatchers.IO) {
            echatModel.writeMessage(chatId, text)
            handleChatLoading(chatId)
        }
    }

    fun onMessageRead(messageDTO: MessageDTO) {
        echatModel.setMessageRead(messageDTO.id)
    }
}