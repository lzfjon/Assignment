package com.learning.assignmentocbc

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.learning.assignmentocbc.model.*
import com.learning.assignmentocbc.network.ApiData
import com.learning.assignmentocbc.network.basicCall
import com.learning.assignmentocbc.network.getResponseFromLogin
import okhttp3.*
import org.junit.Test

import org.junit.Assert.*
import java.io.StringReader

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {


//    private fun getResponseFromLogin(username: String, password: String) : Response {
//        val formBody: RequestBody = FormBody.Builder()
//            .add("username",username)
//            .add("password",password)
//            .build()
//
//        val request : Request = Request.Builder()
//            .url(BASE_URL+"login")
//            .post(formBody)
//            .build()
//
//        return basicCall(request)
//    }
//
//    private fun basicCall(request:Request) :Response {
//        val client : OkHttpClient = OkHttpClient()
//
//        val call : Call = client.newCall(request)
//        return call.execute()
//    }



    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun whenSendPostRequestLogin_thenCorrect() {

        val response = getResponseFromLogin("test","asdasd")
        println(response.body?.string())
        println("1")
        assertEquals(response.code,200)

    }

    @Test
    fun whenSendGetBalance_thenWrong(){
        val request : Request = Request.Builder()
            .url(BASE_URL+"balance")
            .build()

        val response = basicCall(request)
        assertEquals(response.code,401)
    }


    fun getJwtToken() : String {

        val loginResponse = getResponseFromLogin("test","asdasd")

        //To get the response body
        val loginToken = loginResponse.body!!.string()

        println(loginToken)
        println(loginToken::class.simpleName)

        //convert the response body to format that can be taken by Gson to convert
//        val loginTokenJSON = "\"\"\"" + loginToken +"\"\"\""
//        println(loginTokenJSON)
//        println(loginTokenJSON::class.simpleName)

        val test = Gson().fromJson(loginToken,LoginResponse::class.java)
        println(test.toString())
        println(test.token)

        return test.token
    }

    fun buildRequestWithJwtTokenAsHeader(jwtToken: String, Api: String): Request {
        return Request.Builder()
            .url(BASE_URL + Api)
            .addHeader("Authorization", jwtToken)
            .build()
    }

//    fun getJsonStringFromResponse(request: Request) : String{
//
//        return basicCall(request).body!!.string()
//
//
//    }


    @Test
    fun whenSendGetBalance_thenCorrect(){

        val token = getJwtToken()
        val request = buildRequestWithJwtTokenAsHeader(token, "balance")
        val balanceResponse = basicCall(request)
        val test1 = Gson().fromJson(balanceResponse.body!!.string(),AccountBalance::class.java)
        println(test1.balance)
        println(test1.accountNo)


        assertEquals(balanceResponse.code,200)
    }

    @Test
    fun whenSendGetTransactions_thenCorrect(){
        val token = getJwtToken()
        val request = buildRequestWithJwtTokenAsHeader(token, "transactions")
        val transactionResponse = basicCall(request).body!!.string()

        println(transactionResponse)

        val gson = Gson()
        val test1 = gson.fromJson(transactionResponse, ApiTransactionDetails::class.java)
        println(test1::class.simpleName)
        println(test1.data)
        val test1Data = test1.data
        println(test1Data!!::class.java)
        assertEquals(basicCall(request).code,200)
    }

    @Test
    fun whenSendGetPayees_thenCorrect(){

        val token = getJwtToken()
        val request = buildRequestWithJwtTokenAsHeader(token, "payees")
        val payeeResponse = basicCall(request).body!!.string()

        println(payeeResponse)

        val gson = Gson()
        val test1 = gson.fromJson(payeeResponse, ApiPayeeDetails::class.java)
        println(test1::class.simpleName)
        println(test1.data)
        val test1Data = test1.data
        println(test1Data?.get(0)?.id)
        println(test1Data!!::class.java)


        assertEquals(basicCall(request).code,200)


    }


}