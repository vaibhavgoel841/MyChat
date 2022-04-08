package com.example.unmechat

import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.formatAsListItem
import com.example.unmechat.Inbox

import kotlinx.android.synthetic.main.list_item.view.*


class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(item: Inbox, onC: (name: String, photo: String, id: String) -> Unit)
         {
            Log.d("NAMEofuser",item.name.toString())
             Log.d("IMAGEofuser",item.image.toString())
             Log.d("FROMofuser",item.from.toString())
            itemView.countTv.isVisible = item.count > 0
            itemView.countTv.text = item.count.toString()
            itemView.timeTv.text = item.time.formatAsListItem(itemView.context)

            itemView.titleTv.text = item.name
           itemView.subtitleTv.text = item.msg
            Glide.with(itemView.context)
                .load(item.image)
                .placeholder(R.drawable.defaultpi)
                .into(itemView.userImgView)
            itemView.setOnClickListener {

                onC.invoke(item.name, item.image, item.from)
            }
        }
}