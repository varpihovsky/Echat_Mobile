package com.example.echatmobile.profile

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.echatmobile.R
import com.example.echatmobile.databinding.ProfileFragmentBinding
import com.example.echatmobile.di.modules.EchatViewModelFactoryModule
import com.example.echatmobile.system.BaseEvent
import com.example.echatmobile.system.BaseEventTypeInterface
import com.example.echatmobile.system.BaseFragment
import com.example.echatmobile.system.EchatApplication

class ProfileFragment: BaseFragment<ProfileViewModel, ProfileFragmentBinding>() {
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

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        getProfileIdData()
    }

    private fun getProfileIdData(){
        arguments?.run {
            getInt(PROFILE_ID_KEY).let { viewModel.loadProfileData(it) }
        }
    }

    companion object{
        const val PROFILE_ID_KEY = "profile_id"
    }
}