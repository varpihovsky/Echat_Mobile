package com.example.echatmobile.chat

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.echatmobile.R
import com.example.echatmobile.databinding.ChatFragmentBinding
import com.example.echatmobile.di.modules.EchatViewModelFactoryModule
import com.example.echatmobile.system.BaseEventTypeInterface
import com.example.echatmobile.system.BaseFragment
import com.example.echatmobile.system.EchatApplication
import com.example.echatmobile.system.services.MessageService

class ChatFragment : BaseFragment<ChatViewModel, ChatFragmentBinding>() {
    private val dataList = mutableListOf<MessageDTO>()
    private var mBound = false
    private lateinit var binder: MessageService.MessageServiceBinder

    private val connection = object : ServiceConnection {
        var callback: (() -> Unit)? = null
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            binder = service as MessageService.MessageServiceBinder
            mBound = true
            callback?.let { it() }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mBound = false
        }

    }

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

    private fun processNotificationEvent(event: NotificationEvent) {
        val func = {
            if (!event.filtered) binder.clearFilters()
            else binder.setFilteredChatId(event.chatId)
        }
        if (mBound) func() else connection.callback = func
    }

    override fun layoutId(): Int = R.layout.chat_fragment

    override fun viewModelFactory(): ViewModelProvider.AndroidViewModelFactory =
        EchatApplication.instance
            .daggerApplicationComponent
            .plus(EchatViewModelFactoryModule())
            .getChatViewModelFactory()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindService()
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
            ChatAdapter(dataList) { _: ChatAdapter.ViewHolder, i: Int ->
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
        viewModel.data.notificationEvent.observe(viewLifecycleOwner) {
            it.get()?.let { it1 -> processNotificationEvent(it1) }
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

    private fun bindService() {
        context?.bindService(
            Intent(context, MessageService::class.java),
            connection,
            Context.BIND_AUTO_CREATE
        )
    }

    override fun onResume() {
        super.onResume()
        viewModel.onFragmentCreate()
    }

    override fun onPause() {
        super.onPause()
        viewModel.onFragmentDestroy()
    }

    override fun onDetach() {
        super.onDetach()
        viewModel.stopBackgroundThreads()
    }

    companion object {
        const val CHAT_ID_ARGUMENT = "chatId"
    }
}