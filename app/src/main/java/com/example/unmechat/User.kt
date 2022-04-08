package com.example.unmechat

import com.google.firebase.firestore.FieldValue
import java.text.NumberFormat

data class User(
    val name:String,
    val imageUrl:String,
    val thumbImage:String,
    val deviceToken:String,
    val status:String,
    val onlineStatus: String,
    val uid:String
    ){
constructor():this("","","","","", "" ,"")
constructor(name: String,imageUrl: String,thumbImage: String,uid: String):this(
    name,
    imageUrl,
    thumbImage,
    "deviceToken",
    "Hey there I am using UnmeChat",
    "OnlineStatus",
    uid
)



}