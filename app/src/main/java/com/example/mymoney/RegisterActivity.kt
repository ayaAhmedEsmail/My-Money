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
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : AppCompatActivity() {


    private lateinit var sharedPreferences : SharedPreferences
    lateinit var sharedEditor: SharedPreferences.Editor

    public  final val  PREFS_NAME:String = "yes"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        sharedPreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)
         sharedEditor = sharedPreferences.edit()

        txt_login.setOnClickListener {

            val intent =Intent(this,LogInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        btnRegister.setOnClickListener{
            when{
                TextUtils.isEmpty(etEmail.text.toString().trim {it <= ' '})->{
                    Toast.makeText(
                        this@RegisterActivity,
                        "Please enter email.",
                        Toast.LENGTH_LONG
                    ).show()
                }
                TextUtils.isEmpty(etPassword.text.toString().trim{it <= ' '})->{
                    Toast.makeText(
                        this@RegisterActivity,
                        "Please enter valid password.",
                        Toast.LENGTH_LONG
                    ).show()
                }
                else ->{
                    val mail:String = etEmail.text.toString().trim{ it <= ' '}
                    val password:String = etPassword.text.toString().trim{it <= ' '}

                    // Create instance and create register user by mail and password
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(mail,password)
                        .addOnCompleteListener { task ->

                            //if registration success
                            if (task.isSuccessful) {
                                val firebaseUser:FirebaseUser =task.result!!.user!!

                                Toast.makeText(
                                    this,
                                    "You\'re welcome",
                                    Toast.LENGTH_LONG
                                ).show()

                                val intent =Intent(this,MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.putExtra("user_id",firebaseUser.uid)
                                intent.putExtra("email_id",mail)
                                startActivity(intent)
                                finish()
                            }else{
                                // if registration failed
                                Toast.makeText(
                                    this,
                                    task.exception!!.message.toString(),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                }

            }

        }
    }

    override fun onResume() {
        super.onResume()
        if (!sharedPreferences.getBoolean(PREFS_NAME, false)){
            sharedEditor.putBoolean(PREFS_NAME,true)
            sharedEditor.apply()
        }else{
            moveToSec()

        }
    }

    private fun moveToSec(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}