package com.example.echatmobile.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.echatmobile.R
import com.example.echatmobile.databinding.RoomItemBinding
import com.example.echatmobile.model.entities.Chat

class RoomListAdapter(var roomList: List<Chat>) :
    RecyclerView.Adapter<RoomListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = DataBindingUtil.bind<RoomItemBinding>(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.room_item, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding?.roomItemTitle?.text = roomList[position].name

        if(position != roomList.size - 1){
            holder.binding?.roomItemBorderBottom?.visibility = View.INVISIBLE
        }
    }

    override fun getItemCount(): Int = roomList.size
}