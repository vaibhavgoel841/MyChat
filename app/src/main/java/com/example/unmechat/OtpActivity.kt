package com.example.unmechat

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.wifi.hotspot2.pps.Credential
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.view.View.INVISIBLE
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import kotlinx.android.synthetic.main.activity_login.view.*
import kotlinx.android.synthetic.main.activity_otp.*
import androidx.annotation.NonNull
import com.google.firebase.FirebaseApp

import com.google.firebase.FirebaseTooManyRequestsException

import com.google.firebase.FirebaseException
import com.google.firebase.auth.*

import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import java.util.concurrent.TimeUnit



class OtpActivity : AppCompatActivity() {
    lateinit var r:String
     var s:String?=null
    var mVerificationId:String? = null
    private var mCountDownTimer:CountDownTimer?=null
    lateinit var callbacks:PhoneAuthProvider.OnVerificationStateChangedCallbacks
    var mResendToken:PhoneAuthProvider.ForceResendingToken?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        s = intent.getStringExtra("phone")
         r="Waiting to automatically detect an sms send to $s. Wrong Number?"
        tv.text = "Verify $s"
        verifytv.text = r
    setSpannableString();
        showTimer()
        getAuthorised()


verrify.setOnClickListener{
    var code=verifyEt.text.toString()
//    Toast.makeText(this,"verify",Toast.LENGTH_LONG).show()
    if(code.isNotEmpty()&&!mVerificationId.isNullOrBlank()) {
        val credential = PhoneAuthProvider.getCredential(mVerificationId!!, code)
        signWithCredential(credential)
    }

}
        resend.setOnClickListener{
            if(mResendToken!=null){
                showTimer()
PhoneAuthProvider.getInstance().verifyPhoneNumber(s!!,60,TimeUnit.SECONDS,this,callbacks,mResendToken)
            }
        }
     }

    public fun showTimer(){
        resend.isEnabled=false
         mCountDownTimer=object:CountDownTimer(60045,1000){
            override fun onTick(millisUntilFinished: Long) {
                var t=(millisUntilFinished/1000).toString()
                ctv.text="Time Remaining : $t"
            }

            override fun onFinish() {
                resend.isEnabled=true
                ctv.visibility=INVISIBLE

            }

        }
        (mCountDownTimer as CountDownTimer).start();
    }

    fun getAuthorised(){

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
//                Log.d("onAuto","getCode")/
               val smsCode=credential.smsCode
                if(!smsCode.isNullOrBlank())
                    verifyEt.setText(smsCode)
                signWithCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e)

                if (e is FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(this@OtpActivity,"Something Went Wrong Try again",Toast.LENGTH_LONG).show();
                } else if (e is FirebaseTooManyRequestsException) {
                    Toast.makeText(this@OtpActivity,"Too many requests",Toast.LENGTH_LONG).show()
                }
Log.e("What",e.localizedMessage)
              
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:$verificationId")

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId
                mResendToken = token
            }
        }

var auth=PhoneAuthProvider.getInstance()

        auth.verifyPhoneNumber(s!!,60,TimeUnit.SECONDS,this,callbacks)

    }
    fun signWithCredential(credential: PhoneAuthCredential){
        var mauth:FirebaseAuth= FirebaseAuth.getInstance()
        mauth.signInWithCredential(credential)
            .addOnCompleteListener{
                if(it.isSuccessful){
var intent=Intent(this,SignupActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK )
                    intent.putExtra("phoneNumber",s)
                    startActivity(intent)
                    finish()
                }
                else
             Toast.makeText(this,"$mVerificationId ${credential.smsCode}",Toast.LENGTH_LONG).show()
            }
    }
    fun setSpannableString(){
        val span=SpannableString(r)
        val clickableSpan=object:ClickableSpan(){

            override fun onClick(widget: View) {
                var intent=Intent(this@OtpActivity,LoginActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK )
                startActivity(intent)

            }
        }
        span.setSpan(clickableSpan,span.length-13,span.length-1,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
 verifytv.movementMethod=LinkMovementMethod.getInstance()
        verifytv.text=span
    }
    override fun onBackPressed() {

    }

    override fun onDestroy() {
        super.onDestroy()
        if(mCountDownTimer!=null)
        mCountDownTimer!!.cancel()
    }
}
