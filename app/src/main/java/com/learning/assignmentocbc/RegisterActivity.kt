package com.learning.assignmentocbc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView

class RegisterActivity : AppCompatActivity() {

    lateinit var fromRegisterToLogin : Button
    lateinit var registerBackButton : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        fromRegisterToLogin = findViewById(R.id.buttonRegisterConfirm)
        registerBackButton = findViewById(R.id.registerBack)

        fromRegisterToLogin.setOnClickListener {
            var intentRegisterConfirm = Intent(this@RegisterActivity,LoginActivity::class.java)

            startActivity(intentRegisterConfirm)

        }

        registerBackButton.setOnClickListener {
            var intentRegisterBackButton = Intent(this@RegisterActivity,LoginActivity::class.java)

            startActivity(intentRegisterBackButton)
        }






    }
}