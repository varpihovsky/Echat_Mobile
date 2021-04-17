package com.example.echatmobile.login

import com.example.echatmobile.system.BaseEventBuilder
import com.example.echatmobile.system.BaseEventData
import com.example.echatmobile.system.BaseEventTypeInterface
import kotlin.properties.Delegates

enum class AuthorizationEvents : BaseEventTypeInterface{
    CHANGE_AUTHORIZATION_BUTTON{
        var color by Delegates.notNull<Int>()
        var clickable by Delegates.notNull<Boolean>()

        override fun <T : BaseEventData> data(): T = Data() as T

        override fun <T : BaseEventBuilder> builder(): T = Builder() as T

        inner class Builder: ChangeAuthorizationButtonEventBuilder{
            override fun setColor(color: Int): Builder {
                this@CHANGE_AUTHORIZATION_BUTTON.color = color
                return this
            }

            override fun setClickable(clickable: Boolean): Builder {
                this@CHANGE_AUTHORIZATION_BUTTON.clickable = clickable
                return this
            }

            override fun build(): BaseEventTypeInterface = this@CHANGE_AUTHORIZATION_BUTTON
        }

        inner class Data: ChangeAuthorizationButtonEventData{
            override val color: Int
                get() = this@CHANGE_AUTHORIZATION_BUTTON.color
            override val clickable: Boolean
                get() = this@CHANGE_AUTHORIZATION_BUTTON.clickable
        }
    };

    abstract override fun <T : BaseEventBuilder> builder(): T

    abstract override fun <T : BaseEventData> data(): T
}

interface ChangeAuthorizationButtonEventBuilder: BaseEventBuilder{
    fun setColor(color: Int): ChangeAuthorizationButtonEventBuilder
    fun setClickable(clickable: Boolean): ChangeAuthorizationButtonEventBuilder
}

interface ChangeAuthorizationButtonEventData: BaseEventData{
    val color: Int
    val clickable: Boolean
}