package com.example.echatmobile.profile.invites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.echatmobile.R
import com.example.echatmobile.databinding.InviteItemBinding
import com.example.echatmobile.model.entities.InviteDTO

class InvitesListAdapter(
    private val dataList: List<InviteDTO>,
    private val invitesAdapterCallbacks: InvitesAdapterCallbacks
) : RecyclerView.Adapter<InvitesListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = DataBindingUtil.bind<InviteItemBinding>(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.invite_item, parent, false)
        )


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding?.inviteItemChatName?.text = dataList[position].chatDTO.name

        holder.binding?.inviteItemAcceptButton?.setOnClickListener {
            invitesAdapterCallbacks.onAcceptClick(dataList[holder.adapterPosition])
        }
        holder.binding?.inviteItemDeclineButton?.setOnClickListener {
            invitesAdapterCallbacks.onDeclineClick(dataList[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = dataList.size

    interface InvitesAdapterCallbacks {
        fun onAcceptClick(inviteDTO: InviteDTO)

        fun onDeclineClick(inviteDTO: InviteDTO)
    }
}