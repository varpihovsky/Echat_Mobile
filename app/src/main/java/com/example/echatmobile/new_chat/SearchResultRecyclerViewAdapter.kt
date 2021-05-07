package com.example.echatmobile.new_chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.echatmobile.R
import com.example.echatmobile.databinding.JoinChatItemBinding
import com.example.echatmobile.model.entities.Chat
import com.example.echatmobile.model.entities.UserWithoutPassword

class SearchResultRecyclerViewAdapter(
    private val dataList: List<Any>,
    private val searchResultButtonClickListener: SearchResultButtonClickListener
) : RecyclerView.Adapter<SearchResultRecyclerViewAdapter.SearchResultViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder =
        SearchResultViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.join_chat_item, parent, false)
        )

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        when (dataList[position]) {
            is Chat -> processChat(holder, dataList[position] as Chat)
            is UserWithoutPassword -> processUser(holder, dataList[position] as UserWithoutPassword)
        }
    }

    private fun processChat(holder: SearchResultViewHolder, chat: Chat) {
        holder.binding?.joinChatItemTitle?.text = chat.name
        holder.binding?.joinChatItemButton?.apply {
            setText(R.string.search_result_chat_button_text)
            setOnClickListener { searchResultButtonClickListener.onChatButtonClick(dataList[holder.adapterPosition] as Chat) }
        }
    }

    private fun processUser(holder: SearchResultViewHolder, user: UserWithoutPassword) {
        holder.binding?.joinChatItemTitle?.text = user.login
        holder.binding?.joinChatItemButton?.apply {
            setText(R.string.search_result_user_button_text)
            setOnClickListener { searchResultButtonClickListener.onUserButtonClick(dataList[holder.adapterPosition] as UserWithoutPassword) }
        }
    }

    override fun getItemCount(): Int = dataList.size

    class SearchResultViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = DataBindingUtil.bind<JoinChatItemBinding>(view)
    }

    interface SearchResultButtonClickListener {
        fun onChatButtonClick(chat: Chat)

        fun onUserButtonClick(user: UserWithoutPassword)
    }
}