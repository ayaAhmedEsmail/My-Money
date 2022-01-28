package com.example.mymoney



import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.content.Intent
import android.content.SharedPreferences
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_log_in.*
import kotlinx.android.synthetic.main.activity_register.*

class LogInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)


        txt_registration.setOnClickListener {

            val intent =Intent(this,RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        btn_login.setOnClickListener{
            when{
                TextUtils.isEmpty(etEmail_Login.text.toString().trim {it <= ' '})->{
                    Toast.makeText(
                            this@LogInActivity,
                            "Please enter email.",
                            Toast.LENGTH_LONG
                    ).show()
                }
                TextUtils.isEmpty(etPassword_login.text.toString().trim{it <= ' '})->{
                    Toast.makeText(
                            this@LogInActivity,
                            "Please enter valid password.",
                            Toast.LENGTH_LONG
                    ).show()
                }
                else ->{
                    val mail:String = etEmail_Login.text.toString().trim{ it <= ' '}
                    val password:String = etPassword_login.text.toString().trim{it <= ' '}

                    // Create instance and create register user by mail and password
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(mail,password)
                            .addOnCompleteListener { task ->

                                //if registration success
                                if (task.isSuccessful) {
                                    val firebaseUser:FirebaseUser =task.result!!.user!!

                                    Toast.makeText(
                                            this@LogInActivity,
                                            "You\'re Logged in Successfully.. Welcome",
                                            Toast.LENGTH_LONG
                                    ).show()

                                    val intent =Intent(this,MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    intent.putExtra("user_id",FirebaseAuth.getInstance().currentUser!!.uid)
                                    intent.putExtra("email_id",mail)
                                    startActivity(intent)
                                    finish()
                                }else{
                                    // if registration failed
                                    Toast.makeText(
                                            this@LogInActivity,
                                            task.exception!!.message.toString(),
                                            Toast.LENGTH_LONG
                                    ).show()
                                }
                            }

                }

            }

        }

        // Forget password
        txtForgetPassword.setOnClickListener {
            val intent =Intent(this,ForgotPasswordActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}