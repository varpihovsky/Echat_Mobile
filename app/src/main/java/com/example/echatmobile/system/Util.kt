package com.example.echatmobile.system

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun <T> T.handleUnblocking(block: (T) -> Unit) {
    GlobalScope.launch(Dispatchers.IO) { block(this@handleUnblocking) }
}

fun <T> handleUnblocking(block: () -> Unit) {
    GlobalScope.launch(Dispatchers.IO) { block() }
}

fun <T> T.alsoHandleUnblocking(block: (T) -> Unit): T {
    GlobalScope.launch { block(this@alsoHandleUnblocking) }
    return this
}