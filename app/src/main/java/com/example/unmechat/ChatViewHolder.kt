package com.example.unmechat

import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.formatAsListItem
import com.example.unmechat.Inbox
import kotlinx.android.synthetic.main.fragment_people.view.*

import kotlinx.android.synthetic.main.list_item.view.*


class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(item: Inbox, onC: (name: String, photo: String, id: String) -> Unit)=with(itemView)
         {

            countTv.isVisible = item.count > 0
            countTv.text = item.count.toString()
            timeTv.text = item.time.formatAsListItem(itemView.context)

            titleTv.text = item.name
           subTitleTv.text = item.msg
            Glide.with(itemView.context)
                .load(item.image)
                .placeholder(R.drawable.defaultpi)
                .into(itemView.userImgView)
            setOnClickListener {

                onC.invoke(item.name, item.image, item.from)
            }
        }
}