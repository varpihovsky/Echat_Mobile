package com.example.echatmobile.profile

import android.app.Application
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.echatmobile.R
import com.example.echatmobile.model.EchatModel
import com.example.echatmobile.model.entities.UserWithoutPassword
import com.example.echatmobile.system.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(application: Application, private val echatModel: EchatModel) :
    BaseViewModel(application) {
    var user = MutableLiveData<String>()


    fun loadProfileData(profileId: Int){
        GlobalScope.launch(Dispatchers.IO){
            user.postValue(echatModel.getUserProfileById(profileId)?.login)
        }
    }
}