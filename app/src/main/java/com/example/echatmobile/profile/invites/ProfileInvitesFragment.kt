package com.example.echatmobile.profile.invites

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.echatmobile.R
import com.example.echatmobile.databinding.ProfileInvitesFragmentBinding
import com.example.echatmobile.di.modules.EchatViewModelFactoryModule
import com.example.echatmobile.model.entities.Invite
import com.example.echatmobile.new_chat.RemoveDataListItemEvent
import com.example.echatmobile.system.BaseEventTypeInterface
import com.example.echatmobile.system.BaseFragment
import com.example.echatmobile.system.EchatApplication

class ProfileInvitesFragment :
    BaseFragment<ProfileInvitesViewModel, ProfileInvitesFragmentBinding>(),
    InvitesListAdapter.InvitesAdapterCallbacks {
    private val dataList = mutableListOf<Invite>()
    private var listenToDataListEvents = true

    override fun viewModel(): Class<ProfileInvitesViewModel> = ProfileInvitesViewModel::class.java
    override fun viewModelFactory(): ViewModelProvider.AndroidViewModelFactory? =
        EchatApplication.instance.daggerApplicationComponent
            .plus(EchatViewModelFactoryModule())
            .getProfileInvitesViewModelFactory()

    override fun layoutId(): Int = R.layout.profile_invites_fragment

    override fun handleExtendedObservers(baseEvent: BaseEventTypeInterface) {
        when (baseEvent) {
            is RemoveDataListItemEvent -> removeItem(baseEvent.position)
        }
    }

    private fun removeItem(index: Int) {
        dataList.removeAt(index)
        binding.profileRoomList.adapter?.notifyItemRemoved(index)
        listenToDataListEvents = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        initObservers()

        viewModel.onFragmentCreated()
    }

    private fun initRecyclerView() {
        binding.profileRoomList.layoutManager = LinearLayoutManager(context)
        binding.profileRoomList.adapter = InvitesListAdapter(dataList, this)
    }

    private fun initObservers() {
        viewModel.data.dataList.observe(viewLifecycleOwner) { showInvites(it) }
    }

    private fun showInvites(list: List<Invite>) {
        if (listenToDataListEvents) {
            dataList.clear()
            dataList.addAll(list)
            binding.profileRoomList.adapter?.notifyDataSetChanged()
        }
        listenToDataListEvents = true
    }

    override fun onAcceptClick(invite: Invite) {
        viewModel.onInviteAccept(invite)
    }

    override fun onDeclineClick(invite: Invite) {
        viewModel.onInviteDecline(invite)
    }
}