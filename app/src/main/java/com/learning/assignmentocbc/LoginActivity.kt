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
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.learning.assignmentocbc.model.*
import com.learning.assignmentocbc.network.ApiService
import com.learning.assignmentocbc.network.createApiService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.log


const val BASE_URL = "https://green-thumb-64168.uc.r.appspot.com/"

class LoginActivity : AppCompatActivity() {


    lateinit var fromLoginToDashboard: Button
    lateinit var fromLoginToRegister : Button
    lateinit var loginUsername : EditText
    lateinit var loginPassword : EditText

    lateinit var alertUsername : TextView
    lateinit var alertPassword: TextView

    lateinit var errorMessage : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginUsername = findViewById(R.id.loginUsername)
        loginPassword = findViewById(R.id.loginPassword)
        alertUsername = findViewById(R.id.loginUsernameAlert)
        alertPassword = findViewById(R.id.loginPasswordAlert)
        val oriAlertUsername = alertUsername.text
        val oriAlertPassword = alertPassword.text

        errorMessage= findViewById(R.id.loginErrMsg)



        fromLoginToDashboard= findViewById(R.id.buttonLogin)
        fromLoginToRegister = findViewById(R.id.buttonRegister)


        loginUsername.addTextChangedListener (object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {

                if(s.toString().isNotEmpty()){
                    alertUsername.text = ""
                }else{
                    alertUsername.text = oriAlertUsername
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        loginPassword.addTextChangedListener (object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {

                if(s.toString().isNotEmpty()){
                    alertPassword.text = ""
                }else{
                    alertPassword.text = oriAlertPassword
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })



        fromLoginToDashboard.setOnClickListener{



            val usernameInput : String = loginUsername.text.toString()
            val passwordInput : String = loginPassword.text.toString()
            val loginRequest = LoginRequest(usernameInput,passwordInput)

            val failedLogin = MutableLiveData<String>()

            GlobalScope.launch {
                val response = createApiService().postLoginRequest(loginRequest)
                val loginResponse = response.body()

                failedLogin.postValue(loginResponse?.status.toString())

                if(loginResponse?.status == "success"){

                    val token = loginResponse.token
                    val accountName = loginResponse.username
                    val accountNo = loginResponse.accountNo
                    Log.d("Login Act socl" , "$token $accountName $accountNo")


                    val intentDashboard = Intent(this@LoginActivity,DashboardActivity::class.java)
                    intentDashboard.putExtra("token",token)
                    intentDashboard.putExtra("username",accountName)
                    intentDashboard.putExtra("accountNo",accountNo)

                    startActivity(intentDashboard)
                }
            }

            failedLogin.observe(this@LoginActivity, Observer {


                    errorMessage.visibility=View.VISIBLE

            })
        }

        fromLoginToRegister.setOnClickListener{

            val intentRegister = Intent(this@LoginActivity,RegisterActivity::class.java)
            startActivity(intentRegister)

        }
    }

}