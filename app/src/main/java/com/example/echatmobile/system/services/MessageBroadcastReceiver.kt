package com.example.echatmobile.system.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class MessageBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Intent(context, MessageService::class.java).let {
            context?.startService(it)
        }
    }
}