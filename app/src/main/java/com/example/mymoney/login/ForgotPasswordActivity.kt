package com.example.mymoney.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mymoney.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        btn_forgotpassword.setOnClickListener{
            val email:String =editTextTextEmailAddress.text.toString().trim{it <= ' '}

            if(email.isEmpty()){
                Toast.makeText(
                    this@ForgotPasswordActivity,
                    "Please enter email address",
                    Toast.LENGTH_LONG
                ).show()
            }else{
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener{task ->
                        if (task.isSuccessful){
                            Toast.makeText(
                                this@ForgotPasswordActivity,
                                "Email sent successfully to reset you password! ",
                                Toast.LENGTH_LONG
                            ).show()
                            finish()
                        }else{
                            Toast.makeText(
                                this@ForgotPasswordActivity,
                                task.exception!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()
                            finish()
                        }
                    }
            }
        }
    }
}