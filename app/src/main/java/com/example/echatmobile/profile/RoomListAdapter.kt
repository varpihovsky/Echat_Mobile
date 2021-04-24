package com.example.echatmobile.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.echatmobile.R
import com.example.echatmobile.databinding.RoomItemBinding
import com.example.echatmobile.model.entities.Chat

class RoomListAdapter(var roomList: List<Chat>, private val itemClickObject: ItemClickObject) :
    RecyclerView.Adapter<RoomListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = DataBindingUtil.bind<RoomItemBinding>(view)
        var isExpanded = false

        fun expand() {
            binding?.roomAdditional?.visibility = View.VISIBLE
        }

        fun shrink() {
            binding?.roomAdditional?.visibility = View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.room_item, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding?.roomItemTitle?.text = roomList[position].name

        holder.binding?.roomItemTitle?.setOnClickListener {
            itemClickObject.onItemClick(roomList[holder.adapterPosition])
        }
        holder.binding?.roomRemoveButton?.setOnClickListener {
            itemClickObject.onItemRemoveClick(roomList[holder.adapterPosition])
        }
        holder.binding?.roomMoreButton?.setOnClickListener {
            if (holder.isExpanded) {
                holder.shrink()
            } else {
                holder.expand()
            }
        }
    }

    override fun getItemCount(): Int = roomList.size
}

interface ItemClickObject {
    fun onItemClick(chat: Chat)

    fun onItemRemoveClick(chat: Chat)
}