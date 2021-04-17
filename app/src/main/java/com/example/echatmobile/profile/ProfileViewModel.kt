package com.example.echatmobile.profile

import android.app.Application
import com.example.echatmobile.model.EchatModel
import com.example.echatmobile.system.BaseViewModel
import javax.inject.Inject

class ProfileViewModel @Inject constructor(application: Application, echatModel: EchatModel) :
    BaseViewModel(application) {
}