package com.learning.assignmentocbc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginUsername = findViewById(R.id.loginUsername)
        loginPassword = findViewById(R.id.loginPassword)
        alertUsername = findViewById(R.id.loginUsernameAlert)
        alertPassword = findViewById(R.id.loginPasswordAlert)
        val oriAlertUsername = alertUsername.text
        val oriAlertPassword = alertPassword.text


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

            GlobalScope.launch {
                val response = createApiService().postLoginRequest(loginRequest)
                val loginResponse = response.body()

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
        }

        fromLoginToRegister.setOnClickListener{

            val intentRegister = Intent(this@LoginActivity,RegisterActivity::class.java)
            startActivity(intentRegister)

        }
    }



//    fun loginAction(loginRequest: LoginRequest,intentDashboard :Intent){
//
//        val call = createApiService().postLoginRequest(loginRequest)
//
//        call.enqueue(object : Callback<LoginResponse>{
//            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
//
//                if(response.isSuccessful){
//                    val loginResponseBody = response.body()
//                    Log.d("Login Activity",loginResponseBody.toString())
//                    Log.d("Login Activity",loginResponseBody?.token.toString())
//
//                    intentDashboard.putExtra("accountNo",loginResponseBody?.accountNo.toString())
//                    intentDashboard.putExtra("username",loginResponseBody?.username.toString())
//                    intentDashboard.putExtra("token",loginResponseBody?.token.toString())
//                    Log.d("Login Activity", "intent saved")
//
//                }
//
//            }
//
//            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
//                Toast.makeText(applicationContext
//                    ,t.localizedMessage
//                    ,Toast.LENGTH_LONG).show()
//            }
//
//
//        })
//
//
//    }






}