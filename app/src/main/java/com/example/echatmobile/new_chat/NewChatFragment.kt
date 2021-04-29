package com.example.echatmobile.new_chat

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.echatmobile.R
import com.example.echatmobile.databinding.NewChatFragmentBinding
import com.example.echatmobile.di.modules.EchatViewModelFactoryModule
import com.example.echatmobile.model.entities.Chat
import com.example.echatmobile.model.entities.UserWithoutPassword
import com.example.echatmobile.system.BaseEventTypeInterface
import com.example.echatmobile.system.BaseFragment
import com.example.echatmobile.system.EchatApplication

class NewChatFragment : BaseFragment<NewChatViewModel, NewChatFragmentBinding>(),
    AdapterView.OnItemSelectedListener,
    SearchResultRecyclerViewAdapter.SearchResultButtonClickListener {
    private val dataList = mutableListOf<Any>()
    private var reactToDataListChanges = true

    override fun viewModel(): Class<NewChatViewModel> = NewChatViewModel::class.java
    override fun layoutId(): Int = R.layout.new_chat_fragment

    override fun viewModelFactory(): ViewModelProvider.AndroidViewModelFactory =
        EchatApplication.instance
            .daggerApplicationComponent
            .plus(EchatViewModelFactoryModule())
            .getNewChatViewModelFactory()

    override fun handleExtendedObservers(baseEvent: BaseEventTypeInterface) {
        when (baseEvent) {
            is ViewVisibilityEvent -> processVisibility(baseEvent.view, baseEvent.visibility)
            is RemoveDataListItemEvent -> removeDataListItem(baseEvent.position)
        }
    }

    private fun processVisibility(id: String, visibility: Int) {
        when (id) {
            CREATE_BUTTON -> binding.roomCreateButton.visibility = visibility
        }
    }

    private fun removeDataListItem(position: Int) {
        reactToDataListChanges = false
        dataList.removeAt(position)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBinding()
        initSpinner()
        initRecyclerView()
        initObservers()
    }

    private fun initBinding() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun initSpinner() {
        context?.let {
            binding.searchTypeSpinner.adapter =
                ArrayAdapter.createFromResource(
                    it,
                    R.array.search_type,
                    R.layout.support_simple_spinner_dropdown_item
                )
            binding.searchTypeSpinner.onItemSelectedListener = this
        }
    }

    private fun initRecyclerView() {
        binding.roomSearchResultList.layoutManager = LinearLayoutManager(context)
        binding.roomSearchResultList.adapter = SearchResultRecyclerViewAdapter(dataList, this)
    }

    private fun initObservers() {
        viewModel.data.dataList.observe(viewLifecycleOwner) { handleList(it) }
    }

    private fun handleList(list: List<Any>) {
        if (!reactToDataListChanges) {
            reactToDataListChanges = true
            return
        }

        dataList.clear()
        dataList.addAll(list)
        binding.roomSearchResultList.adapter?.notifyDataSetChanged()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (position) {
            0 -> viewModel.setRoomsSearchTypeSelected()
            1 -> viewModel.setUsersSearchTypeSelected()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun onChatButtonClick(chat: Chat) {
        viewModel.onChatButtonClick(chat)
    }

    override fun onUserButtonClick(user: UserWithoutPassword) {
        viewModel.onUserButtonClick(user)
    }

    companion object {
        const val CREATE_BUTTON = "create"
    }
}