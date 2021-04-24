package com.example.echatmobile.chat

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.echatmobile.R
import com.example.echatmobile.databinding.ChatItemBinding

class ChatAdapter(
    private val dataList: List<MessageDTO>,
    private val onBindViewHolderCallback: (ViewHolder, Int) -> Unit
) :
    RecyclerView.Adapter<ChatAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = DataBindingUtil.bind<ChatItemBinding>(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.chat_item, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        fillViewHolder(holder, position)
        onBindViewHolderCallback(holder, position)
    }

    private fun setMessageAligning(holder: ViewHolder, gravityAlign: Int) {
        holder.binding?.chatItemCard?.layoutParams =
            (holder.binding?.chatItemCard?.layoutParams as FrameLayout.LayoutParams).apply {
                gravity = gravityAlign
            }
    }

    @SuppressLint("SetTextI18n")
    private fun fillViewHolder(holder: ViewHolder, position: Int) {
        holder.binding?.chatText?.text = dataList[position].text
        holder.binding?.chatTitle?.text =
            dataList[position].sender.login + if (dataList[position].receiver != null) {
                " answers to " + dataList[position].receiver?.sender?.login
            } else {
                ""
            }
        holder.binding?.chatMessageDate?.text = dataList[position].created.subSequence(0, 10)
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)

        if (dataList[holder.adapterPosition].align == ALIGN_RIGHT) {
            setMessageAligning(holder, Gravity.END)
        }
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        setMessageAligning(holder, Gravity.START)
    }

    override fun getItemCount(): Int = dataList.size
}