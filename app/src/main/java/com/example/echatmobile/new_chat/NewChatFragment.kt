package com.example.echatmobile.new_chat

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.echatmobile.R
import com.example.echatmobile.databinding.NewChatFragmentBinding
import com.example.echatmobile.di.EchatViewModelFactoryComponent
import com.example.echatmobile.model.entities.ChatDTO
import com.example.echatmobile.model.entities.UserWithoutPassword
import com.example.echatmobile.system.BaseEventTypeInterface
import com.example.echatmobile.system.components.ui.architecture.ListableFragment

class NewChatFragment : ListableFragment<NewChatViewModel, NewChatFragmentBinding, Any>(),
    AdapterView.OnItemSelectedListener,
    SearchResultRecyclerViewAdapter.SearchResultButtonClickListener {

    override fun viewModel(): Class<NewChatViewModel> = NewChatViewModel::class.java
    override fun layoutId(): Int = R.layout.new_chat_fragment
    override fun viewModelFactorySelector(): (EchatViewModelFactoryComponent.() -> ViewModelProvider.AndroidViewModelFactory) =
        provideViewModelSelector { getNewChatViewModelFactory() }

    override fun onListReplacedCallback() {
        binding.roomSearchResultList.adapter?.notifyDataSetChanged()
    }

    override fun onListUpdatedCallback(updateType: String, index: Int) {
        binding.roomSearchResultList.adapter?.notifyItemRemoved(index)
    }

    override fun handleExtendedObservers(baseEvent: BaseEventTypeInterface) {
        when (baseEvent) {
            is ViewVisibilityEvent -> processVisibility(baseEvent.view, baseEvent.visibility)
            else -> super.handleExtendedObservers(baseEvent)
        }
    }

    private fun processVisibility(id: String, visibility: Int) {
        when (id) {
            CREATE_BUTTON -> binding.roomCreateButton.visibility = visibility
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBinding()
        initSpinner()
        initRecyclerView()
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

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (position) {
            0 -> viewModel.setRoomsSearchTypeSelected()
            1 -> viewModel.setUsersSearchTypeSelected()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun onChatButtonClick(chatDTO: ChatDTO) {
        viewModel.onChatButtonClick(chatDTO)
    }

    override fun onUserButtonClick(user: UserWithoutPassword) {
        viewModel.onUserButtonClick(user)
    }

    companion object {
        const val CREATE_BUTTON = "create"
    }
}