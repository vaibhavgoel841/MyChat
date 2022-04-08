package com.example.unmechat

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.isSameDayAs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.EmojiPopup
import com.vanniktech.emoji.google.GoogleEmojiProvider
import kotlinx.android.synthetic.main.activity_chat.*
import java.sql.Types.NULL
const val USER_ID = "userId"
const val USER_THUMB_IMAGE = "thumbImage"
const val USER_NAME = "userName"
class ChatActivity : AppCompatActivity() {

    private val  friendId:String by lazy {
     intent.getStringExtra("UID")!!
 }
    private val name :String by lazy { intent.getStringExtra("NAME")!! }
    private val image:String by lazy {
        intent.getStringExtra("IMAGE")!!
    }

    private val mcurrentUser :String by lazy{
        FirebaseAuth.getInstance().uid!!

       }
    private val db: FirebaseDatabase by lazy{
        FirebaseDatabase.getInstance()
    }
    lateinit var currentUser: User
    private var messages= mutableListOf<ChatEvent>()
    lateinit var chatAdapter: ChatAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EmojiManager.install(GoogleEmojiProvider())
        setContentView(R.layout.activity_chat)
        nameTv.text=name
        supportActionBar?.hide()

        Glide.with(this).load(image).into(userImgView);
FirebaseFirestore.getInstance().collection("users").document(mcurrentUser).get().
addOnSuccessListener  {

    currentUser=it.toObject(User::class.java)!!
}
        listenToMessages()
        chatAdapter= ChatAdapter(messages,mcurrentUser)
        msgRv.apply {
            adapter=chatAdapter
            layoutManager=LinearLayoutManager(this@ChatActivity)
        }
        val emojiPopup = EmojiPopup.Builder.fromRootView(rootView).build(msgEdtv)
        smileBtn.setOnClickListener {
            emojiPopup.toggle()
        }
sendBtn.setOnClickListener{
    msgEdtv.text?.let{
        if(!it.isNullOrBlank()){
            sendMessage(it.toString())
            it.clear()
        }

    }
}

    }
private fun listenToMessages(){
    getMessages(friendId).orderByKey().addChildEventListener(object:ChildEventListener{
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val msg:Message?=snapshot.getValue(Message::class.java)
            addMessage(msg)
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

        }

        override fun onChildRemoved(snapshot: DataSnapshot) {

        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

        }

        override fun onCancelled(error: DatabaseError) {

        }


    })
}

    private fun addMessage(msg: Message?) {
val eventBefore=messages.lastOrNull()
        if (msg != null) {
            if((eventBefore!=null&&!eventBefore.sentAt.isSameDayAs(msg!!.sentAt))||eventBefore==null){
                if (msg != null) {
                    messages.add(DateHeader(msg.sentAt,this))
                }
            }

                messages.add(msg);
            chatAdapter.notifyItemChanged(messages.size-1)
            msgRv.scrollToPosition(messages.size-1)
        }
    }

    private fun sendMessage(msg: String) {
val id=getMessages(friendId).push().key
        checkNotNull(id){"cannot be null"}
        val msgMap=Message(msg,mcurrentUser,id)
        getMessages(friendId).child(id).setValue(msgMap).addOnSuccessListener {

        }
updateLastMessage(msgMap)

    }
private fun updateLastMessage(message: Message){
    val inboxMap=Inbox(
        message.msg,friendId,name,image,count=0)

    getInbox(mcurrentUser,friendId).setValue(inboxMap)
    getInbox(friendId,mcurrentUser).addListenerForSingleValueEvent(object:ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            val value=snapshot.getValue(Inbox::class.java)
            inboxMap.apply {
                from=message.msgId;
                name=currentUser.name
                image=currentUser.thumbImage
                count=1;
            }
           value?.let {
               if(it.from==message.senderId)
inboxMap.count=value.count+1;
           }
        }

        override fun onCancelled(error: DatabaseError) {

        }

    })
    getInbox(friendId,mcurrentUser).setValue(inboxMap)
}
    private fun marksasRead(){
        getInbox(friendId,mcurrentUser).child("count").setValue(0)
    }
    private fun getMessages(friendId:String)=db.reference.child("messages/${getId(friendId)}")
    private fun getInbox(toUser: String,fromUser: String)=db.reference.child("chats/$toUser/$fromUser")

    private fun getId(friendId: String):String{
        return if(friendId>mcurrentUser)
            friendId+mcurrentUser;
        else
            mcurrentUser+friendId
    }

}


