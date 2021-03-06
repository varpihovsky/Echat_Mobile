package com.example.echatmobile.chat

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.echatmobile.model.EchatModel
import com.example.echatmobile.model.NoInternetConnectionException
import com.example.echatmobile.model.entities.MessageDTO
import com.example.echatmobile.system.BaseEvent
import com.example.echatmobile.system.components.ListableViewModel
import com.example.echatmobile.system.components.events.ClearChatFieldEvent
import com.example.echatmobile.system.components.events.MoveDownEvent
import com.example.echatmobile.system.components.events.NotificationEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.properties.Delegates

class ChatViewModel @Inject constructor(
    application: Application,
    private val echatModel: EchatModel
) : ListableViewModel<MessageViewModelDTO>(application) {
    val data by lazy { Data() }
    private val notificationEvent = MutableLiveData<BaseEvent<NotificationEvent>>()
    private var chatId by Delegates.notNull<Long>()
    private var areBackgroundThreadsRunning = true

    inner class Data {
        val notificationEvent: LiveData<BaseEvent<NotificationEvent>> =
            this@ChatViewModel.notificationEvent
    }

    fun loadChat(chatId: Long) {
        this.chatId = chatId
        GlobalScope.launch(Dispatchers.IO) { handleIO { handleChatLoading(chatId) } }
        areBackgroundThreadsRunning = true
        GlobalScope.launch(Dispatchers.IO) {
            handleNotReadMessages()
        }
    }

    private fun handleChatLoading(chatId: Long) {
        val messagesDTO = retrieveMessagesList(chatId)
        addAllToList(messagesDTO)
    }

    private fun retrieveMessagesList(chatId: Long) =
        echatModel.getMessageHistory(chatId)
            .apply { let { it[0] } }
            .map { mapMessageAligning(it) }

    private suspend fun handleNotReadMessages() {
        while (areBackgroundThreadsRunning) {
            delay(CHAT_LOADING_DELAY)
            var recentMessages: List<MessageViewModelDTO> = listOf()
            handleIO {
                try {
                    recentMessages = echatModel.getNotReadMessages()
                        .map { mapMessageAligning(it) }
                        .filter {
                            it.chatDTO.id == chatId && listableData.dataList.value?.contains(
                                it
                            ) == false
                        }
                } catch (e: NoInternetConnectionException) {
                }
                notifyUIAboutChatUpdates(recentMessages)
            }
        }
    }

    private fun notifyUIAboutChatUpdates(recentMessages: List<MessageViewModelDTO>) {
        recentMessages.forEach { addToList(it) }
    }

    fun onSendButtonClick(text: String) {
        baseEventLiveData.value = BaseEvent(ClearChatFieldEvent())
        GlobalScope.launch(Dispatchers.IO) { handleIO { handleMessageSending(text) } }
    }

    private fun handleMessageSending(text: String) {
        echatModel.writeMessage(chatId, text)
        retrieveMessagesList(chatId).filter { listableData.dataList.value?.contains(it) == false }
            .forEach {
                addToList(it)
            }
    }

    fun onMessageRead(messageDTO: MessageViewModelDTO) {
        GlobalScope.launch(Dispatchers.IO) { handleIO { echatModel.setMessageRead(messageDTO.id) } }
    }

    fun onFragmentDetach() {
        areBackgroundThreadsRunning = false
    }

    fun onMoveDownButtonClick() {
        baseEventLiveData.value = BaseEvent(MoveDownEvent())
    }

    fun onFragmentDestroy() {
        notificationEvent.value = BaseEvent(NotificationEvent(false, chatId))
    }

    fun onFragmentCreate() {
        notificationEvent.value = BaseEvent(NotificationEvent(true, chatId))
    }

    private fun mapMessageAligning(messageDTO: MessageDTO): MessageViewModelDTO =
        messageDTO.toDTO { messageDTO ->
            if (messageDTO.sender.login == echatModel.currentUserLogin) {
                ALIGN_RIGHT
            } else {
                ALIGN_LEFT
            }

        }

    companion object {
        private const val CHAT_LOADING_DELAY = 500L
    }
}