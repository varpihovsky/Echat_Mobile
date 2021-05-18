package com.example.echatmobile.profile.rooms

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.echatmobile.R
import com.example.echatmobile.databinding.ProfileRoomsFragmentBinding
import com.example.echatmobile.di.EchatViewModelFactoryComponent
import com.example.echatmobile.model.entities.ChatDTO
import com.example.echatmobile.profile.ItemClickObject
import com.example.echatmobile.profile.ProfileFragment
import com.example.echatmobile.profile.RoomListAdapter
import com.example.echatmobile.system.components.ui.ListableFragment

class ProfileRoomsFragment :
    ListableFragment<ProfileRoomsViewModel, ProfileRoomsFragmentBinding, ChatDTO>(),
    ItemClickObject {
    private lateinit var profileFragment: ProfileFragment

    override fun viewModel(): Class<ProfileRoomsViewModel> = ProfileRoomsViewModel::class.java

    override fun viewModelFactorySelector(): (EchatViewModelFactoryComponent.() -> ViewModelProvider.AndroidViewModelFactory) =
        provideViewModelSelector { getProfileRoomsViewModelFactory() }

    override fun layoutId(): Int = R.layout.profile_rooms_fragment

    override fun onListReplacedCallback() {
        binding.profileRoomList.adapter?.notifyDataSetChanged()
    }

    override fun onListUpdatedCallback(updateType: String, index: Int) {
        binding.profileRoomList.adapter?.notifyItemRemoved(index)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (parentFragment?.parentFragment as ProfileFragment).addOnCreatedTask {
            profileFragment = parentFragment?.parentFragment as ProfileFragment

            profileFragment.getViewModel().addOnUserLoadCallback {
                initRecyclerView()

                viewModel.onFragmentCreated(it)
            }
        }
    }

//    override fun onStart() {
//        super.onStart()
//
//        profileFragment = parentFragment?.parentFragment as ProfileFragment
//
//        profileFragment.getViewModel().addOnUserLoadCallback {
//            initRecyclerView()
//
//            viewModel.onFragmentCreated(it)
//        }
//    }

    private fun initRecyclerView() {
        binding.profileRoomList.layoutManager = LinearLayoutManager(context)
        binding.profileRoomList.adapter = RoomListAdapter(dataList, this).let {
            if (!profileFragment.getViewModel().isUnnecessaryDataShown()) {
                it.setButtonsShown(false)
            }
            it
        }
    }

    override fun onItemClick(chatDTO: ChatDTO) {
        viewModel.onItemClick(chatDTO)
    }

    override fun onItemRemoveClick(chatDTO: ChatDTO) {
        viewModel.onItemRemoveClick(chatDTO)
    }

    override fun onInviteClick(chatDTO: ChatDTO) {
        viewModel.onInviteClick(chatDTO)
    }
}