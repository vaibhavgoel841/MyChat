package com.example.unmechat

import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.list_item.view.*

class UserViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
    fun bind(user:User, onClick: (name:String,imageUrl:String,id:String) -> Unit){

        Log.d("vaifind",user.name)
        itemView.countTv.isVisible=false;
        itemView.timeTv.isVisible=false;
        itemView.titleTv.text=user.name

        itemView.subtitleTv.text=user.status
        Glide.with(itemView.context)
            .load(user.imageUrl)
            .into(itemView.userImgView);
     itemView.setOnClickListener{
         onClick.invoke(user.name,user.thumbImage,user.uid)

     }

    }
}