package com.example.unmechat

import  android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_signup.*
import java.util.jar.Manifest

class SignupActivity : AppCompatActivity() {

    lateinit var s:String
    val storage by lazy{
FirebaseStorage.getInstance()
    }
    val auth by lazy{
        FirebaseAuth.getInstance()
    }
    val database by lazy{
        FirebaseFirestore.getInstance()
    }
    lateinit var downloadUrl:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        profileImg.setOnClickListener {
            checkPermissionforImage()
        }
        nextBtn.setOnClickListener {
            it.isEnabled=false
            val name = et.text.toString()
            if (name.isEmpty())
                Toast.makeText(this, "Name Cannot be empty", Toast.LENGTH_LONG).show()
            else if(!::downloadUrl.isInitialized)
                Toast.makeText(this,"Image cannot be empty",Toast.LENGTH_LONG).show()
            else {

                val user=User(name,downloadUrl,downloadUrl,auth.uid.toString())
database.collection("users").document(auth.uid!!).set(user).addOnSuccessListener {
    val intent=Intent(this,MainActivity::class.java)
    startActivity(intent)
    finish()
}.addOnFailureListener{
    nextBtn.isEnabled=true
}
            }
        }
    }


    private fun checkPermissionforImage() {
if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED
    &&ContextCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED){
    val permission= arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    ActivityCompat.requestPermissions(this,permission,1001)
    pickImageFromGallery()
}
        else{
pickImageFromGallery()
        }
    }
    fun pickImageFromGallery(){
        var intent=Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent,1002)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK&&requestCode==1002){


                profileImg.setImageURI(data?.data)
uploadImage(data?.data!!)
        }
    }

    private fun uploadImage(it: Uri) {
nextBtn.isEnabled=false
val ref=storage.reference.child("uploads/"+auth.uid.toString())
        val uploadTask=ref.putFile(it)
uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot,Task<Uri>>{task->
if(!task.isSuccessful){
   throw task.exception!!
}
return@Continuation ref.downloadUrl
}).addOnCompleteListener{task->
    if(task.isSuccessful ){
        downloadUrl=task.result.toString()
        nextBtn.isEnabled=true;
        Log.i("URL","downloadUrl= $downloadUrl")
    }else{

    }
}.addOnFailureListener{

}

    }
}

