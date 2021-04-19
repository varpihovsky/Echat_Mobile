package com.example.echatmobile.profile

import android.app.Application
import android.util.Log
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.echatmobile.R
import com.example.echatmobile.model.EchatModel
import com.example.echatmobile.model.entities.Chat
import com.example.echatmobile.model.entities.User
import com.example.echatmobile.model.entities.UserWithoutPassword
import com.example.echatmobile.system.BaseViewModel
import com.example.echatmobile.system.EchatApplication.Companion.LOG_TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    application: Application,
    private val echatModel: EchatModel
) :
    BaseViewModel(application) {
    val data: Data
    val user = MutableLiveData<UserWithoutPassword>()
    private val chatList = MutableLiveData<List<Chat>>()

    init {
        data = Data()
    }

    inner class Data {
        val chatList: LiveData<List<Chat>> = this@ProfileViewModel.chatList
    }

    fun loadProfileData(profileId: Long?) {
        GlobalScope.launch(Dispatchers.IO) {
            if(profileId == null){
                handleCurrentProfile()
            } else{
                handleProfile(profileId)
            }
        }
    }

    private fun handleCurrentProfile(){
        echatModel.getCurrentUserProfile()?.let {
            UserWithoutPassword(it.id, it.login)
        }?.let { postData(it) }
    }

    private fun handleProfile(profileId: Long){
        echatModel.getUserProfileById(profileId)?.let { postData(it) }
    }

    private fun postData(userWithoutPassword: UserWithoutPassword){
        user.postValue(userWithoutPassword)
        chatList.postValue(echatModel.getChatsByParticipantId(userWithoutPassword.id))
    }

    fun onNewChatButtonClick(){
        navigate(R.id.action_profileFragment_to_newChatFragment)
    }
}