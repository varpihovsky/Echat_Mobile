package com.example.echatmobile.model.util

import com.example.echatmobile.system.ConnectionManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ConnectionListener(private val connectionCallbacks: ConnectionCallbacks) {
    var run: Boolean = true
    private val connectionManager = ConnectionManager()
    private var currentConnection = connectionManager.isConnected()

    init {
        GlobalScope.launch { listenConnection() }
    }

    private suspend fun listenConnection() {
        while (run) {
            delay(500)
            connectionManager.isConnected().let {
                if (it != currentConnection) {
                    connectionCallbacks.onConnectionChange(it)
                    currentConnection = it
                }
            }
        }
    }

    interface ConnectionCallbacks {
        fun onConnectionChange(state: Boolean)
    }
}