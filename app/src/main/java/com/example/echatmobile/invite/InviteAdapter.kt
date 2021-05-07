package com.example.echatmobile.invite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.echatmobile.R
import com.example.echatmobile.databinding.JoinChatItemBinding
import com.example.echatmobile.model.entities.UserWithoutPassword

class InviteAdapter(
    private val dataList: List<UserWithoutPassword>,
    private val inviteButtonClickListener: InviteButtonClickListener
) :
    RecyclerView.Adapter<InviteAdapter.Holder>() {
    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = DataBindingUtil.bind<JoinChatItemBinding>(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
        Holder(LayoutInflater.from(parent.context).inflate(R.layout.join_chat_item, parent, false))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding?.joinChatItemTitle?.text = dataList[position].login
        holder.binding?.joinChatItemButton?.setText(R.string.room_invite_button_text)
        holder.binding?.joinChatItemButton?.setOnClickListener {
            inviteButtonClickListener.onInviteButtonClick(
                dataList[holder.adapterPosition]
            )
        }
    }

    override fun getItemCount(): Int = dataList.size

    interface InviteButtonClickListener {
        fun onInviteButtonClick(userWithoutPassword: UserWithoutPassword)
    }
}