package com.example.echatmobile.profile.invites

import android.app.Application
import com.example.echatmobile.model.EchatModel
import com.example.echatmobile.model.entities.Invite
import com.example.echatmobile.system.components.ListableViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileInvitesViewModel @Inject constructor(
    application: Application,
    private val echatModel: EchatModel
) : ListableViewModel<Invite>(application) {
    fun onFragmentCreated() {
        if (isListEmpty()) {
            GlobalScope.launch(Dispatchers.IO) { handleIO { initDataList() } }
        }
    }

    private fun initDataList() {
        addAllToList(echatModel.getCurrentUserInvites())
    }

    fun onInviteAccept(invite: Invite) {
        GlobalScope.launch(Dispatchers.IO) { handleIO { processInvite(invite) { acceptInvite(invite.id) } } }
    }

    fun onInviteDecline(invite: Invite) {
        GlobalScope.launch(Dispatchers.IO) { handleIO { processInvite(invite) { declineInvite(invite.id) } } }
    }

    private fun processInvite(invite: Invite, inviteMethod: EchatModel.() -> Unit) {
        inviteMethod(echatModel)
        removeFromList(invite)
    }
}