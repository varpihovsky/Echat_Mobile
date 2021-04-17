package com.example.echatmobile.profile

import androidx.lifecycle.ViewModelProvider
import com.example.echatmobile.R
import com.example.echatmobile.databinding.ProfileFragmentBinding
import com.example.echatmobile.system.BaseEvent
import com.example.echatmobile.system.BaseEventTypeInterface
import com.example.echatmobile.system.BaseFragment

class ProfileFragment: BaseFragment<ProfileViewModel, ProfileFragmentBinding>() {
    override fun viewModel(): Class<ProfileViewModel> = ProfileViewModel::class.java
    override fun viewModelFactory(): ViewModelProvider.AndroidViewModelFactory? = null
    override fun layoutId(): Int = R.layout.profile_fragment
    override fun handleExtendedObservers(baseEvent: BaseEvent<BaseEventTypeInterface>) {}


}