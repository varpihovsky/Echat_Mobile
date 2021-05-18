package com.example.echatmobile.system.components.events

import com.example.echatmobile.system.BaseEventTypeInterface

class ChangeAuthorizationButtonEvent(val color: Int, val clickable: Boolean) :
    BaseEventTypeInterface