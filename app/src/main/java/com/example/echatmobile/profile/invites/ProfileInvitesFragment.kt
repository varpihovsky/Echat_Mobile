package com.example.echatmobile.profile.invites

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.echatmobile.R
import com.example.echatmobile.databinding.ProfileInvitesFragmentBinding
import com.example.echatmobile.di.EchatViewModelFactoryComponent
import com.example.echatmobile.model.entities.InviteDTO
import com.example.echatmobile.system.components.ui.architecture.ListableFragment

class ProfileInvitesFragment :
    ListableFragment<ProfileInvitesViewModel, ProfileInvitesFragmentBinding, InviteDTO>(),
    InvitesListAdapter.InvitesAdapterCallbacks {

    override fun viewModel(): Class<ProfileInvitesViewModel> = ProfileInvitesViewModel::class.java

    override fun viewModelFactorySelector(): (EchatViewModelFactoryComponent.() -> ViewModelProvider.AndroidViewModelFactory) =
        provideViewModelSelector { getProfileInvitesViewModelFactory() }

    override fun layoutId(): Int = R.layout.profile_invites_fragment

    override fun onListReplacedCallback() {
        binding.profileRoomList.adapter?.notifyDataSetChanged()
    }

    override fun onListUpdatedCallback(updateType: String, index: Int) {
        binding.profileRoomList.adapter?.notifyItemRemoved(index)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        viewModel.onFragmentCreated()
    }

    private fun initRecyclerView() {
        binding.profileRoomList.layoutManager = LinearLayoutManager(context)
        binding.profileRoomList.adapter = InvitesListAdapter(dataList, this)
    }

    override fun onAcceptClick(inviteDTO: InviteDTO) {
        viewModel.onInviteAccept(inviteDTO)
    }

    override fun onDeclineClick(inviteDTO: InviteDTO) {
        viewModel.onInviteDecline(inviteDTO)
    }
}