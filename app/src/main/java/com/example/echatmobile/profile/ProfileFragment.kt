package com.example.echatmobile.profile

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.echatmobile.R
import com.example.echatmobile.databinding.ProfileFragmentBinding
import com.example.echatmobile.di.EchatViewModelFactoryComponent
import com.example.echatmobile.system.BaseEventTypeInterface
import com.example.echatmobile.system.BaseFragment
import com.example.echatmobile.system.components.events.HideUnnecessaryDataEvent
import com.example.echatmobile.system.components.events.NavigateEvent

class ProfileFragment : BaseFragment<ProfileViewModel, ProfileFragmentBinding>() {
    private lateinit var navController: NavController

    override fun viewModel(): Class<ProfileViewModel> = ProfileViewModel::class.java
    override fun layoutId(): Int = R.layout.profile_fragment

    override fun viewModelFactorySelector(): (EchatViewModelFactoryComponent.() -> ViewModelProvider.AndroidViewModelFactory) =
        provideViewModelSelector { getProfileViewModelFactory() }

    override fun handleExtendedObservers(baseEvent: BaseEventTypeInterface) {
        when (baseEvent) {
            is NavigateEvent -> navigate(baseEvent.destination)
            is HideUnnecessaryDataEvent -> hideUnnecessary()
        }
    }

    private fun navigate(destination: Int) {
        try {
            navController.navigate(destination)
        } catch (e: IllegalArgumentException) {
            // Same button clicked. Nothing to do.
        }
    }

    private fun hideUnnecessary() {
        binding.profileChatsButton.visibility = View.GONE
        binding.profileInvitesButton.visibility = View.GONE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBinding()
        getProfileIdData()
        initNavController()
    }

    private fun initBinding() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun getProfileIdData() {
        val profileId = arguments?.getLong(PROFILE_ID_KEY)
        viewModel.loadProfileData(profileId)
    }

    private fun initNavController() {
        navController =
            (childFragmentManager.findFragmentById(R.id.profile_result_fragment) as NavHostFragment).navController
    }

    fun getViewModel() = viewModel

    companion object {
        const val PROFILE_ID_KEY = "profile_id"
    }
}