package com.example.echatmobile.chat

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.echatmobile.di.DaggerContextComponent
import com.example.echatmobile.di.modules.ContextModule
import com.example.echatmobile.model.EchatModel
import com.example.echatmobile.model.entities.Message
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
) : ListableViewModel<MessageDTO>(application) {

    init {
        DaggerContextComponent.builder().contextModule(ContextModule(application)).build()
            .inject(echatModel)
    }

    val data by lazy { Data() }
    private val notificationEvent = MutableLiveData<BaseEvent<NotificationEvent>>()
    private var chatId by Delegates.notNull<Long>()
    private var areBackgroundThreadsRunning = true

    private val showedRecentMessages = mutableListOf<MessageDTO>()

    inner class Data {
        val notificationEvent: LiveData<BaseEvent<NotificationEvent>> =
            this@ChatViewModel.notificationEvent
    }

    fun loadChat(chatId: Long) {
        this.chatId = chatId
        GlobalScope.launch(Dispatchers.IO) { handleIO { handleChatLoading(chatId) } }
        areBackgroundThreadsRunning = true
        GlobalScope.launch(Dispatchers.IO) { handleNotReadMessages() }
    }

    private suspend fun handleNotReadMessages() {
        while (areBackgroundThreadsRunning) {
            delay(CHAT_LOADING_DELAY)
            val recentMessages = echatModel.getNotReadMessages()
                .map { mapMessageAligning(it) }
                .filter { it.chat.id == chatId && !showedRecentMessages.contains(it) }
            showedRecentMessages.addAll(recentMessages)
            notifyUIAboutChatUpdates(recentMessages)
        }
    }

    private fun notifyUIAboutChatUpdates(recentMessages: List<MessageDTO>) {
        recentMessages.forEach { addToList(it) }
    }

    fun onSendButtonClick(text: String) {
        baseEventLiveData.value = BaseEvent(ClearChatFieldEvent())
        GlobalScope.launch(Dispatchers.IO) {
            echatModel.writeMessage(chatId, text)
            handleChatLoading(chatId)
        }
    }

    private fun handleChatLoading(chatId: Long) {
        val messagesDTO = retrieveMessagesList(chatId)
        addAllToList(messagesDTO)
    }

    private fun retrieveMessagesList(chatId: Long) =
        echatModel.getMessageHistory(chatId).map { mapMessageAligning(it) }

    private fun mapMessageAligning(message: Message): MessageDTO =
        message.toDTO { messageDTO ->
            if (messageDTO.sender.login == echatModel.currentUserLogin) {
                ALIGN_RIGHT
            } else {
                ALIGN_LEFT
            }

        }

    fun onMessageRead(messageDTO: MessageDTO) {
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

    companion object {
        private const val CHAT_LOADING_DELAY = 500L
    }
}