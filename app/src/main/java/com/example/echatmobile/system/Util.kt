package com.example.echatmobile.system

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun <T> T.letHandleUnblocking(block: (T) -> Unit) {
    GlobalScope.launch(Dispatchers.IO) { block(this@letHandleUnblocking) }
}

fun <T> T.handleUnblocking(block: () -> Unit) {
    GlobalScope.launch(Dispatchers.IO) { block() }
}

fun <T> T.alsoHandleUnblocking(block: (T) -> Unit): T {
    GlobalScope.launch(Dispatchers.IO) { block(this@alsoHandleUnblocking) }
    return this
}