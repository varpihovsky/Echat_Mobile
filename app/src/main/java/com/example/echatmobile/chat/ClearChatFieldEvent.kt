package com.example.echatmobile.chat

import com.example.echatmobile.system.BaseEventBuilder
import com.example.echatmobile.system.BaseEventData
import com.example.echatmobile.system.BaseEventType
import com.example.echatmobile.system.BaseEventTypeInterface

class ClearChatFieldEvent : BaseEventTypeInterface {
    override fun <T : BaseEventBuilder> builder(): T {
        BaseEventType.EMPTY.throwMarkerTypeException()
    }

    override fun <T : BaseEventData> data(): T {
        BaseEventType.EMPTY.throwMarkerTypeException()
    }
}