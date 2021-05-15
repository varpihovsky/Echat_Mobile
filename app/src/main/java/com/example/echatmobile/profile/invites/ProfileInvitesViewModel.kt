package com.example.echatmobile.profile.invites

import android.app.Application
import com.example.echatmobile.model.EchatModel
import com.example.echatmobile.model.entities.InviteDTO
import com.example.echatmobile.system.components.ListableViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileInvitesViewModel @Inject constructor(
    application: Application,
    private val echatModel: EchatModel
) : ListableViewModel<InviteDTO>(application) {
    fun onFragmentCreated() {
        if (isListEmpty()) {
            GlobalScope.launch(Dispatchers.IO) { handleIO { initDataList() } }
        }
    }

    private fun initDataList() {
        addAllToList(echatModel.getCurrentUserInvites())
    }

    fun onInviteAccept(inviteDTO: InviteDTO) {
        GlobalScope.launch(Dispatchers.IO) {
            handleIO {
                processInvite(inviteDTO) {
                    acceptInvite(
                        inviteDTO.id
                    )
                }
            }
        }
    }

    fun onInviteDecline(inviteDTO: InviteDTO) {
        GlobalScope.launch(Dispatchers.IO) {
            handleIO {
                processInvite(inviteDTO) {
                    declineInvite(
                        inviteDTO.id
                    )
                }
            }
        }
    }

    private fun processInvite(inviteDTO: InviteDTO, inviteMethod: EchatModel.() -> Unit) {
        inviteMethod(echatModel)
        removeFromList(inviteDTO)
    }
}