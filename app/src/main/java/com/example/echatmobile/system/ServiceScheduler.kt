package com.example.echatmobile.system

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.echatmobile.system.services.MessageBroadcastReceiver

class ServiceScheduler(private val context: Context) {
    private val alarmManager by lazy { context.getSystemService(Context.ALARM_SERVICE) as AlarmManager }

    fun scheduleMessageService() {
        val pendingIntent =
            PendingIntent.getBroadcast(
                context,
                0,
                Intent(context, MessageBroadcastReceiver::class.java),
                0
            )
        if (pendingIntent != null) {
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + 60000,
                pendingIntent
            )
        }
    }
}