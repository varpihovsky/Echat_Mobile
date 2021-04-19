package com.example.echatmobile.new_chat

import android.app.Application
import android.widget.Toast
import com.example.echatmobile.R
import com.example.echatmobile.model.EchatModel
import com.example.echatmobile.system.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class NewChatViewModel @Inject constructor(
    application: Application,
    private val echatModel: EchatModel
) :
    BaseViewModel(application) {
    fun onCreateButtonClick(roomName: String) {
        if (roomName.isEmpty()) {
            makeToast("Cant create room with empty name", Toast.LENGTH_SHORT)
            return
        }

        GlobalScope.launch(Dispatchers.IO){ handleCreation(roomName)}
    }

    private fun handleCreation(roomName: String){
        try {
            echatModel.createChat(roomName)?.let {
                makeToast("Chat created", Toast.LENGTH_SHORT)
                return
            }
            makeToast("Chat wasn't created, please try another name", Toast.LENGTH_SHORT)
        } catch (e: Exception){
            e.message?.let { makeToast(it, Toast.LENGTH_SHORT) }
        }
    }

    fun onProfileClick(){
        navigate(R.id.action_newChatFragment_to_profileFragment)
    }

}