package com.example.echatmobile.invite

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.echatmobile.R
import com.example.echatmobile.databinding.InviteFragmentBinding
import com.example.echatmobile.di.EchatViewModelFactoryComponent
import com.example.echatmobile.model.entities.UserWithoutPassword
import com.example.echatmobile.new_chat.RemoveDataListItemEvent
import com.example.echatmobile.system.BaseEventTypeInterface
import com.example.echatmobile.system.BaseFragment

class InviteFragment : BaseFragment<InviteViewModel, InviteFragmentBinding>(),
    InviteAdapter.InviteButtonClickListener {
    private val dataList = mutableListOf<UserWithoutPassword>()
    private var listenToDataListChanges = true

    override fun viewModel(): Class<InviteViewModel> = InviteViewModel::class.java

    override fun viewModelFactorySelector(): (EchatViewModelFactoryComponent.() -> ViewModelProvider.AndroidViewModelFactory) =
        provideViewModelSelector { getInviteViewModelFactory() }

    override fun layoutId(): Int = R.layout.invite_fragment

    override fun handleExtendedObservers(baseEvent: BaseEventTypeInterface) {
        when (baseEvent) {
            is RemoveDataListItemEvent -> removeItem(baseEvent.position)
        }
    }

    private fun removeItem(position: Int) {
        listenToDataListChanges = false
        dataList.removeAt(position)
        binding.inviteSearchResult.adapter?.notifyItemRemoved(position)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBinding()
        initRecyclerView()
        initArguments()
        initObservers()
    }

    private fun initBinding() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun initRecyclerView() {
        binding.inviteSearchResult.layoutManager = LinearLayoutManager(context)
        binding.inviteSearchResult.adapter = InviteAdapter(dataList, this)
    }

    override fun onInviteButtonClick(userWithoutPassword: UserWithoutPassword) {
        viewModel.onInviteButtonClick(userWithoutPassword)
    }

    private fun initArguments() {
        arguments?.let {
            viewModel.setChatId(it.getLong(CHAT_ID_PARAM))
        }
    }

    private fun initObservers() {
        viewModel.data.dataList.observe(viewLifecycleOwner) {
            showSearchResult(it)
        }
    }

    private fun showSearchResult(list: List<UserWithoutPassword>) {
        if (listenToDataListChanges) {
            dataList.clear()
            dataList.addAll(list)
            binding.inviteSearchResult.adapter?.notifyDataSetChanged()
        }
        listenToDataListChanges = true
    }

    companion object {
        const val CHAT_ID_PARAM = "chatId"
    }
}