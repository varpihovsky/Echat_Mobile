package com.example.echatmobile.systemimport android.os.Bundleimport kotlin.properties.Delegatesclass BaseEvent<T : BaseEventTypeInterface>(val eventType: T)interface BaseEventTypeInterface{    fun <T : BaseEventBuilder> builder(): T    fun <T : BaseEventData> data(): T}enum class BaseEventType: BaseEventTypeInterface {    TOAST_STRING {        lateinit var text: String            private set        var length by Delegates.notNull<Int>()            private set        override fun <T : BaseEventBuilder> builder(): T = Builder() as T        override fun <T : BaseEventData> data(): T = Data() as T        inner class Builder : ToastStringBuilder {            override fun setText(text: String): ToastStringBuilder {                this@TOAST_STRING.text = text                return this            }            override fun setLength(length: Int): ToastStringBuilder {                this@TOAST_STRING.length = length                return this            }            override fun build() = this@TOAST_STRING        }        inner class Data: ToastStringData{            override val text: String                get() = this@TOAST_STRING.text            override val length: Int                get() = this@TOAST_STRING.length        }    },    TOAST_RESOURCE {        var resource by Delegates.notNull<Int>()        var length by Delegates.notNull<Int>()        override fun <T : BaseEventBuilder> builder(): T = Builder() as T        override fun <T : BaseEventData> data(): T = Data() as T        inner class Builder : ToastResourceBuilder {            override fun setResource(resource: Int): ToastResourceBuilder {                this@TOAST_RESOURCE.resource = resource                return this            }            override fun setLength(length: Int): ToastResourceBuilder {                this@TOAST_RESOURCE.length = length                return this            }            override fun build() = this@TOAST_RESOURCE        }        inner class Data: ToastResourceData{            override val resource: Int                get() = this@TOAST_RESOURCE.resource            override val length: Int                get() = this@TOAST_RESOURCE.length        }    }, HIDE_KEYBOARD{        override fun <T : BaseEventBuilder> builder(): T {            throwMarkerTypeException()        }        override fun <T : BaseEventData> data(): T {            throwMarkerTypeException()        }    }, NAVIGATE{        var action by Delegates.notNull<Int>()        var navigationData: Bundle? = null        override fun <T : BaseEventBuilder> builder(): T = Builder() as T        override fun <T : BaseEventData> data(): T = Data() as T        inner class Builder: NavigateEventBuilder{            override fun setAction(action: Int): NavigateEventBuilder {                this@NAVIGATE.action = action                return this            }            override fun setNavigationData(bundle: Bundle?): NavigateEventBuilder {                navigationData = bundle                return this            }            override fun build(): BaseEventType = this@NAVIGATE        }        inner class Data : NavigateEventData {            override val action: Int                get() = this@NAVIGATE.action            override val data: Bundle?                get() = navigationData        }    },    EMPTY {        override fun <T : BaseEventBuilder> builder(): T {            throwMarkerTypeException()        }        override fun <T : BaseEventData> data(): T {            throwMarkerTypeException()        }    };    abstract override fun <T : BaseEventBuilder> builder(): T    abstract override fun <T : BaseEventData> data(): T    fun throwMarkerTypeException(): Nothing {        throw RuntimeException("This type is marker, cant reach any data or builder")    }}interface BaseEventBuilder{    fun build(): BaseEventTypeInterface}interface ToastBuilder: BaseEventBuilder{    fun setLength(length: Int): ToastBuilder}interface ToastResourceBuilder: ToastBuilder{    fun setResource(resource: Int):ToastResourceBuilder}interface ToastStringBuilder: ToastBuilder{    fun setText(text: String): ToastStringBuilder}interface NavigateEventBuilder: BaseEventBuilder{    fun setAction(action: Int): NavigateEventBuilder    fun setNavigationData(bundle: Bundle?): NavigateEventBuilder}interface BaseEventDatainterface ToastEventData: BaseEventData{    val length: Int}interface ToastResourceData: ToastEventData{    val resource: Int}interface ToastStringData: ToastEventData{    val text: String}interface NavigateEventData: BaseEventData{    val action: Int    val data: Bundle?}