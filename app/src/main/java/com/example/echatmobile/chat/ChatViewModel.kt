package com.example.echatmobile.chat

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.echatmobile.model.EchatModel
import com.example.echatmobile.model.entities.Message
import com.example.echatmobile.system.BaseEvent
import com.example.echatmobile.system.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.properties.Delegates

class ChatViewModel @Inject constructor(
    application: Application,
    private val echatModel: EchatModel
) : BaseViewModel(application) {

    val data by lazy { Data() }
    private val messagesLiveData = MutableLiveData<List<MessageDTO>>()
    private val chatUpdateLiveData = MutableLiveData<MessageDTO?>()
    private var chatId by Delegates.notNull<Long>()
    private var areNotBackgroundThreadsRunning = false

    private val showedRecentMessages = mutableListOf<MessageDTO>()

    inner class Data {
        val messagesLiveData: LiveData<List<MessageDTO>> = this@ChatViewModel.messagesLiveData

        //TODO: wrap chatUpdateLiveData with BaseEvent and put it to baseEventLiveData
        val chatUpdateLiveData: LiveData<MessageDTO?> = this@ChatViewModel.chatUpdateLiveData
    }

    fun loadChat(chatId: Long) {
        this.chatId = chatId
        GlobalScope.launch(Dispatchers.IO) { handleChatLoading(chatId) }
        areNotBackgroundThreadsRunning = false
        GlobalScope.launch(Dispatchers.IO) { handleNotReadMessages() }
    }

    private suspend fun handleNotReadMessages() {
        while (!areNotBackgroundThreadsRunning) {
            delay(CHAT_LOADING_DELAY)
            val recentMessages = echatModel.getNotReadMessages()
                .map { mapMessageAligning(it) }
                .filter { it.chat.id == chatId && !showedRecentMessages.contains(it) }
            showedRecentMessages.addAll(recentMessages)
            GlobalScope.launch(Dispatchers.Main) { notifyUIAboutChatUpdates(recentMessages) }
        }
    }

    private fun notifyUIAboutChatUpdates(recentMessages: List<MessageDTO>) {
        recentMessages.forEach {
            chatUpdateLiveData.value = it
        }
        chatUpdateLiveData.value = null
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
            mapMessageAligning(it)
        }

    private fun mapMessageAligning(message: Message): MessageDTO =
        message.toDTO { messageDTO ->
            if (messageDTO.sender.login == echatModel.currentUserLogin) {
                ALIGN_RIGHT
            } else {
                ALIGN_LEFT
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
        GlobalScope.launch(Dispatchers.IO) { echatModel.setMessageRead(messageDTO.id) }
    }

    fun stopBackgroundThreads() {
        areNotBackgroundThreadsRunning = true
    }

    companion object {
        private const val CHAT_LOADING_DELAY = 500L
    }
}