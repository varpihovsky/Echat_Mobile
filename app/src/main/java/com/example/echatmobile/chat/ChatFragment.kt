package com.example.echatmobile.chat

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.echatmobile.R
import com.example.echatmobile.databinding.ChatFragmentBinding
import com.example.echatmobile.di.modules.EchatViewModelFactoryModule
import com.example.echatmobile.system.BaseEventTypeInterface
import com.example.echatmobile.system.BaseFragment
import com.example.echatmobile.system.EchatApplication

class ChatFragment : BaseFragment<ChatViewModel, ChatFragmentBinding>() {
    private val dataList = mutableListOf<MessageDTO>()

    override fun viewModel(): Class<ChatViewModel> = ChatViewModel::class.java
    override fun handleExtendedObservers(baseEvent: BaseEventTypeInterface) {
        when (baseEvent) {
            is ClearChatFieldEvent -> binding.chatTextField.setText("")
            is MoveDownEvent -> moveDown()
        }
    }

    private fun moveDown() {
        binding.chatMessageList.layoutManager?.scrollToPosition(dataList.lastIndex)
    }

    override fun layoutId(): Int = R.layout.chat_fragment

    override fun viewModelFactory(): ViewModelProvider.AndroidViewModelFactory =
        EchatApplication.instance
            .daggerApplicationComponent
            .plus(EchatViewModelFactoryModule())
            .getChatViewModelFactory()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBinding()
        initRecyclerView()
        initObservers()
        initArguments()
    }

    private fun initBinding() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun initRecyclerView() {
        binding.chatMessageList.adapter =
            ChatAdapter(dataList) { viewHolder: ChatAdapter.ViewHolder, i: Int ->
                viewModel.onMessageRead(dataList[i])
            }
        binding.chatMessageList.layoutManager = LinearLayoutManager(context)
    }

    private fun initObservers() {
        viewModel.data.messagesLiveData.observe(viewLifecycleOwner) {
            showChat(it)
        }
        viewModel.data.chatUpdateLiveData.observe(viewLifecycleOwner) {
            it?.let { messageDTO -> loadRecentMessage(messageDTO) }
        }
    }

    private fun showChat(messages: List<MessageDTO>) {
        dataList.removeAll { true }
        dataList.addAll(messages)
        binding.chatMessageList.adapter?.notifyDataSetChanged()
    }

    private fun loadRecentMessage(messageDTO: MessageDTO) {
        dataList.add(messageDTO)
        binding.chatMessageList.adapter?.notifyItemInserted(dataList.size - 1)
    }

    private fun initArguments() {
        arguments?.let {
            viewModel.loadChat(it.getLong(CHAT_ID_ARGUMENT))
        }
        binding.chatMessageList.layoutManager?.scrollToPosition(dataList.size - 1)
    }

    override fun onDetach() {
        super.onDetach()
        viewModel.stopBackgroundThreads()
    }

    companion object {
        const val CHAT_ID_ARGUMENT = "chatId"
    }
}