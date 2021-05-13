package com.example.echatmobile.profile.rooms

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.echatmobile.R
import com.example.echatmobile.databinding.ProfileRoomsFragmentBinding
import com.example.echatmobile.di.EchatViewModelFactoryComponent
import com.example.echatmobile.model.entities.Chat
import com.example.echatmobile.new_chat.RemoveDataListItemEvent
import com.example.echatmobile.profile.ItemClickObject
import com.example.echatmobile.profile.ProfileFragment
import com.example.echatmobile.profile.RoomListAdapter
import com.example.echatmobile.system.BaseEventTypeInterface
import com.example.echatmobile.system.BaseFragment

class ProfileRoomsFragment : BaseFragment<ProfileRoomsViewModel, ProfileRoomsFragmentBinding>(),
    ItemClickObject {
    private val dataList = mutableListOf<Chat>()
    private var listenToDataListEvents = true
    private lateinit var profileFragment: ProfileFragment

    override fun viewModel(): Class<ProfileRoomsViewModel> = ProfileRoomsViewModel::class.java

    override fun viewModelFactorySelector(): (EchatViewModelFactoryComponent.() -> ViewModelProvider.AndroidViewModelFactory) =
        provideViewModelSelector { getProfileRoomsViewModelFactory() }

    override fun layoutId(): Int = R.layout.profile_rooms_fragment

    override fun handleExtendedObservers(baseEvent: BaseEventTypeInterface) {
        when (baseEvent) {
            is RemoveDataListItemEvent -> removeDataListItem(baseEvent.position)
        }
    }

    private fun removeDataListItem(index: Int) {
        dataList.removeAt(index)
        binding.profileRoomList.adapter?.notifyItemRemoved(index)
        listenToDataListEvents = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (parentFragment?.parentFragment as ProfileFragment).addOnCreatedTask {
            profileFragment = parentFragment?.parentFragment as ProfileFragment

            profileFragment.getViewModel().addOnUserLoadCallback {
                initRecyclerView()
                initObservers()

                viewModel.onFragmentCreated(it)
            }
        }
    }

    private fun initRecyclerView() {
        binding.profileRoomList.layoutManager = LinearLayoutManager(context)
        binding.profileRoomList.adapter = RoomListAdapter(dataList, this).let {
            if (!profileFragment.getViewModel().isUnnecessaryDataShown()) {
                it.setButtonsShown(false)
            }
            it
        }
    }

    private fun initObservers() {
        viewModel.data.dataList.observe(viewLifecycleOwner) { loadList(it) }
    }

    private fun loadList(list: List<Chat>) {
        if (listenToDataListEvents) {
            dataList.clear()
            dataList.addAll(list)
            binding.profileRoomList.adapter?.notifyDataSetChanged()
        }
        listenToDataListEvents = true
    }

    override fun onItemClick(chat: Chat) {
        viewModel.onItemClick(chat)
    }

    override fun onItemRemoveClick(chat: Chat) {
        viewModel.onItemRemoveClick(chat)
    }

    override fun onInviteClick(chat: Chat) {
        viewModel.onInviteClick(chat)
    }
}