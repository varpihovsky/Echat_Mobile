package com.example.echatmobile.model

class WrongLoginOrPasswordException(message: String) : RuntimeException(message)

class NoInternetConnectionException(message: String) : java.lang.RuntimeException(message)