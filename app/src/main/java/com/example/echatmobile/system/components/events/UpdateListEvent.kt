package com.example.echatmobile.system.components.events

import com.example.echatmobile.system.BaseEventTypeInterface

data class UpdateListEvent<T>(val data: T, val updateType: String) : BaseEventTypeInterface {
    init {
        if (updateType != ADD || updateType != REMOVE) {
            throw RuntimeException("Unsupported update type")
        }
    }

    companion object {
        const val ADD = "create"
        const val REMOVE = "remove"
    }
}