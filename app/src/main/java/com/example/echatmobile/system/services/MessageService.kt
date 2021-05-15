package com.example.echatmobile.system.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.*
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import com.example.echatmobile.MainActivity
import com.example.echatmobile.R
import com.example.echatmobile.chat.ChatFragment
import com.example.echatmobile.model.entities.MessageDTO
import com.example.echatmobile.system.EchatApplication
import com.example.echatmobile.system.ServiceScheduler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MessageService : Service() {
    private var filteredChatId: Long? = null
    private val echatModel by lazy { EchatApplication.instance.daggerEchatModelComponent.provideModel() }

    private val serviceScheduler = ServiceScheduler(this)

    override fun onBind(intent: Intent?): IBinder = MessageServiceBinder()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()

        GlobalScope.launch(Dispatchers.IO) { handleMessageList() }

        serviceScheduler.scheduleMessageService()
        return START_STICKY
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel =
                NotificationChannel(CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance).apply {
                    description = NOTIFICATION_CHANNEL_DESCRIPTION
                }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun handleMessageList() {
        echatModel.getNotReadMessages().filter { it.chat.id != filteredChatId }.let {
            setMessagesRead(it)
            GlobalScope.launch(Dispatchers.Main) { showMassages(it) }
        }
    }

    private fun showMassages(list: List<MessageDTO>) {
        list.forEach { message ->
            createNotification("${message.sender.login}: ${message.text}", message)
        }
    }

    private fun setMessagesRead(list: List<MessageDTO>) {
        list.forEach { message -> echatModel.setMessageRead(message.id) }
    }

    private fun createNotification(text: String, messageDTO: MessageDTO) =
        Notification.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(NOTIFICATION_TITLE)
            .setContentText(text)
            .setContentIntent(
                NavDeepLinkBuilder(this)
                    .setComponentName(MainActivity::class.java)
                    .setDestination(R.id.chatFragment)
                    .setArguments(Bundle().apply {
                        putLong(
                            ChatFragment.CHAT_ID_ARGUMENT,
                            messageDTO.chat.id
                        )
                    })
                    .setGraph(R.navigation.navigation)
                    .createPendingIntent()
            ).let { NotificationManagerCompat.from(this).notify(counter++, it.build()) }

    fun setFilteredChatId(id: Long) {
        filteredChatId = id
    }

    fun clearFilters() {
        filteredChatId = null
    }

    inner class MessageServiceBinder : Binder() {
        fun setFilteredChatId(id: Long) = this@MessageService.setFilteredChatId(id)
        fun clearFilters() = this@MessageService.clearFilters()
    }

    companion object {
        private var counter: Int = 0
        private const val CHANNEL_ID = "channel_foreground"
        private const val NOTIFICATION_CHANNEL_NAME = "Echat mobile messaging"
        private const val NOTIFICATION_CHANNEL_DESCRIPTION =
            "This notification channel provides you" +
                    "info about recent messages"
        private const val NOTIFICATION_TITLE = "Echat Mobile - new message"
    }
}