package com.example.unmechat

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.formatAsTime
import kotlinx.android.synthetic.main.list_item_chat_recv_message.view.*
import kotlinx.android.synthetic.main.list_item_chat_recv_message.view.*
import kotlinx.android.synthetic.main.list_item_date_header.view.*

class ChatAdapter(private val list:MutableList<ChatEvent>,private val mCurrentUserId:String): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val receivedView=LayoutInflater.from(parent.context).inflate(R.layout.list_item_chat_recv_message,parent,false);
        val sendView=LayoutInflater.from(parent.context).inflate(R.layout.list_item_chatsend_message,parent,false);
        val dateHeaderView=LayoutInflater.from(parent.context).inflate(R.layout.list_item_date_header,parent,false)
        return when(viewType){

   TEXT_MESSAGE_RECEIVED-> {
       MessageViewHolder(receivedView)
   }
    TEXT_MESSAGE_SEND->{
MessageViewHolder(sendView)
    }
    DATE_HEADER->{
DateViewHolder(dateHeaderView)
    }
    else -> MessageViewHolder(receivedView)
}
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
when(val item=list[position]){
    is DateHeader->{
        if(holder.itemView.textView==null)
Log.d("check","Holder is NULL")
        else
        holder.itemView.textView.text=item.date
    }
    is Message->{
        if(holder==null)
            Log.d("check","Holder is NULL")
        else{
        holder.itemView.apply {
            content.text = item.msg
            time.text = item.sentAt.formatAsTime()
        }
        }
    }
}
    }

    override fun getItemCount(): Int {
return list.size

    }

    override fun getItemViewType(position: Int): Int {
       return  when (val event=list[position]){
           is Message->{
                if(event.senderId==mCurrentUserId)
                    TEXT_MESSAGE_SEND
               else
                   TEXT_MESSAGE_RECEIVED
           }
           is DateHeader-> DATE_HEADER
           else -> UNSUPPORTED
       }
    }
    class DateViewHolder(view:View):RecyclerView.ViewHolder(view)
    class MessageViewHolder(view: View):RecyclerView.ViewHolder(view)


    companion object{
        private const val UNSUPPORTED=-1
        private const val TEXT_MESSAGE_RECEIVED=0
        private const val TEXT_MESSAGE_SEND=1
        private const val DATE_HEADER=2
    }

}