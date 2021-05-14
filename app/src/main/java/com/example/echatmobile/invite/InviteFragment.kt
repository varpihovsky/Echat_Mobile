package com.example.echatmobile.invite

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.echatmobile.R
import com.example.echatmobile.databinding.InviteFragmentBinding
import com.example.echatmobile.di.EchatViewModelFactoryComponent
import com.example.echatmobile.model.entities.UserWithoutPassword
import com.example.echatmobile.system.components.ListableFragment

class InviteFragment :
    ListableFragment<InviteViewModel, InviteFragmentBinding, UserWithoutPassword>(),
    InviteAdapter.InviteButtonClickListener {
    override fun viewModel(): Class<InviteViewModel> = InviteViewModel::class.java

    override fun viewModelFactorySelector(): (EchatViewModelFactoryComponent.() -> ViewModelProvider.AndroidViewModelFactory) =
        provideViewModelSelector { getInviteViewModelFactory() }

    override fun layoutId(): Int = R.layout.invite_fragment

    override fun onListReplacedCallback() {
        binding.inviteSearchResult.adapter?.notifyDataSetChanged()
    }

    override fun onListUpdatedCallback(updateType: String, index: Int) {
        binding.inviteSearchResult.adapter?.notifyItemRemoved(index)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBinding()
        initRecyclerView()
        initArguments()
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

    companion object {
        const val CHAT_ID_PARAM = "chatId"
    }
}