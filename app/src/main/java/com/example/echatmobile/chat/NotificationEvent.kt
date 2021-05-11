package com.example.echatmobile.chat

import com.example.echatmobile.system.BaseEventTypeInterface

class NotificationEvent(val filtered: Boolean, val chatId: Long) : BaseEventTypeInterface