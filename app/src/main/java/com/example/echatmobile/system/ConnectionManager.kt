package com.example.echatmobile.system

import java.net.InetAddress

class ConnectionManager {
    fun isConnected(): Boolean =
        try {
            InetAddress.getByName(DEFAULT_HOST)
            true
        } catch (e: java.lang.Exception) {
            false
        }

    companion object {
        private const val DEFAULT_HOST = "google.com"
    }
}