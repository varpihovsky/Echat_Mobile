package com.example.echatmobile.invite

import android.app.Application
import com.example.echatmobile.model.EchatModel
import com.example.echatmobile.model.entities.UserWithoutPassword
import com.example.echatmobile.system.BaseFragment.Companion.TOAST_SHORT
import com.example.echatmobile.system.components.ui.architecture.ListableViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class InviteViewModel @Inject constructor(
    application: Application,
    private val echatModel: EchatModel
) : ListableViewModel<UserWithoutPassword>(application) {
    private var chatId: Long = -1

    fun setChatId(chatId: Long) {
        this.chatId = chatId
    }

    fun onSearchButtonClick(username: String) {
        if (!isChatIdAssigned()) {
            return
        }

        GlobalScope.launch(Dispatchers.IO) { processSearch(username) }
    }

    private fun isChatIdAssigned(): Boolean {
        if (chatId < 0) {
            makeToast("Unforeseen behaviour. Please try to reopen invite screen", TOAST_SHORT)
            return false
        }
        return true
    }

    private fun processSearch(username: String) {
        addAllToList(echatModel.getUserProfileByQuery(username))
    }

    fun onInviteButtonClick(user: UserWithoutPassword) {
        GlobalScope.launch(Dispatchers.IO) { handleIO { handleInvite(user) } }
    }

    private fun handleInvite(user: UserWithoutPassword) {
        echatModel.invite(chatId, user.id)
        removeFromList(user)
    }
}