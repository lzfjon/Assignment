package com.learning.assignmentocbc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.learning.assignmentocbc.model.RegisterRequest
import com.learning.assignmentocbc.network.createApiService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.PasswordAuthentication

class RegisterActivity : AppCompatActivity() {

    lateinit var fromRegisterToLogin : Button
    lateinit var registerBackButton : ImageView

    lateinit var registerUsername : EditText
    lateinit var registerPassword : EditText
    lateinit var confirmRegisterPassword : EditText
    lateinit var confirmPasswordAlert : TextView

    lateinit var registerError : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        fromRegisterToLogin = findViewById(R.id.buttonRegisterConfirm)
        registerBackButton = findViewById(R.id.registerBack)
        registerUsername = findViewById(R.id.registerUsername)
        registerPassword = findViewById(R.id.registerPassword)
        confirmRegisterPassword = findViewById(R.id.registerConfirmPassword)
        confirmPasswordAlert = findViewById(R.id.registerConfirmPasswordAlert)
//        confirmPasswordAlert.visibility=View.GONE
        registerError = findViewById(R.id.registerErrMsg)

        registerPassword.addTextChangedListener( object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                checkPassword()
            }

        })


        confirmRegisterPassword.addTextChangedListener( object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                checkPassword()
            }

        })




        fromRegisterToLogin.setOnClickListener {
            val registerUsername = registerUsername.text.toString()
            val registerPassword = registerPassword.text.toString()
            val confirmRegisterPassword = confirmRegisterPassword.text.toString()

            val registerRequest = RegisterRequest(registerUsername,registerPassword)
            val failedRegister = MutableLiveData<String>()
            var intentRegisterConfirm = Intent(this@RegisterActivity,DashboardActivity::class.java)



            GlobalScope.launch {

                var token : String = ""
                val response = createApiService().postRegister(registerRequest)
                Log.d("Register Activity", "response = $response")

                failedRegister.postValue(response.toString())

                if(response.code()!=200){
                    Log.d("Register Activity", "response = "+response.code())


                } else{
                    val requestResponse = response.body()
                    Log.d("Register Activity", requestResponse.toString())
                    token = requestResponse?.token.toString();

                    intentRegisterConfirm.putExtra("token", token)
                    intentRegisterConfirm.putExtra("username",registerUsername)
                    startActivity(intentRegisterConfirm)

                }
            }

            failedRegister.observe(this@RegisterActivity, Observer {

                registerError.visibility = View.VISIBLE
            })


        }

        registerBackButton.setOnClickListener {
            var intentRegisterBackButton = Intent(this@RegisterActivity,LoginActivity::class.java)

            startActivity(intentRegisterBackButton)
        }

    }


    fun checkPassword(){
        val password  = registerPassword.text.toString()
        val confirmPassword = confirmRegisterPassword.text.toString()
        if(password.isNotEmpty() || confirmPassword.isNotEmpty()){
            if(confirmPassword != password){
                confirmPasswordAlert.visibility = View.VISIBLE
            }else{
                confirmPasswordAlert.visibility=View.GONE
            }

        }else{
            confirmPasswordAlert.visibility=View.GONE
        }
    }
}