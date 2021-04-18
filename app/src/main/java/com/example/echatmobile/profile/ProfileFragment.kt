package com.example.echatmobile.profile

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.echatmobile.R
import com.example.echatmobile.databinding.ProfileFragmentBinding
import com.example.echatmobile.di.modules.EchatViewModelFactoryModule
import com.example.echatmobile.system.BaseEvent
import com.example.echatmobile.system.BaseEventTypeInterface
import com.example.echatmobile.system.BaseFragment
import com.example.echatmobile.system.EchatApplication
import com.example.echatmobile.system.EchatApplication.Companion.LOG_TAG

class ProfileFragment: BaseFragment<ProfileViewModel, ProfileFragmentBinding>() {
    private val adapter = RoomListAdapter(mutableListOf())

    override fun viewModel(): Class<ProfileViewModel> = ProfileViewModel::class.java
    override fun layoutId(): Int = R.layout.profile_fragment
    override fun handleExtendedObservers(baseEvent: BaseEvent<BaseEventTypeInterface>) {}

    override fun viewModelFactory(): ViewModelProvider.AndroidViewModelFactory =
        EchatApplication.instance
            .daggerApplicationComponent
            .plus(EchatViewModelFactoryModule())
            .getProfileViewModelFactory()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBinding()
        initObservers()
        getProfileIdData()
    }

    private fun initBinding(){
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.profileRoomList.adapter = adapter
        binding.profileRoomList.layoutManager = LinearLayoutManager(context)
    }

    private fun initObservers(){
        viewModel.data.chatList.observe(viewLifecycleOwner){
            Log.d(LOG_TAG, "Got an chat list: $it")
            adapter.roomList = it
            adapter.notifyDataSetChanged()
        }
    }

    private fun getProfileIdData(){
        arguments?.run {
            getLong(PROFILE_ID_KEY).let { viewModel.loadProfileData(it) }
        }
    }

    companion object{
        const val PROFILE_ID_KEY = "profile_id"
    }
}