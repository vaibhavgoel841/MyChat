 package com.example.unmechat

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_login.*
import com.hbb20.CountryCodePicker




 class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
button.isEnabled=false
edittext.addTextChangedListener {
    if (edittext.length() >= 10)
        button.isEnabled = true
    if (edittext.length() < 10)
        button.isEnabled = false

}




        button.setOnClickListener{v->
            Alert(edittext.text.toString())
        }

    }
     fun Alert(message:String){
         var str:String=ccp.selectedCountryCodeWithPlus+message
         val builder=AlertDialog.Builder(this)

         builder.setTitle("We will be verifying the phone number $str")
             .setMessage("IS it Ok or would to like to edit number")
             .setPositiveButton("Ok"){dialogIntere,wh->
                 showOtpActivity(str)

             }
             .setNegativeButton("Edit"){dialogInterface,wh->
                 builder.create().dismiss()

             }

         val alertDialog=builder.create()
         alertDialog.setCancelable(true)
         alertDialog.show()
     }

     private fun showOtpActivity(st:String) {
var i=Intent(this,OtpActivity::class.java)
         i.putExtra("phone",st)
         startActivity(i)
     }
 }