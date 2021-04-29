package com.example.echatmobile.new_chat

import com.example.echatmobile.system.BaseEventBuilder
import com.example.echatmobile.system.BaseEventData
import com.example.echatmobile.system.BaseEventTypeInterface

class ViewVisibilityEvent(val view: String, val visibility: Int) : BaseEventTypeInterface {
    override fun <T : BaseEventBuilder> builder(): T {
        TODO("Not yet implemented")
    }

    override fun <T : BaseEventData> data(): T {
        TODO("Not yet implemented")
    }
}