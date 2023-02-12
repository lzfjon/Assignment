package com.learning.assignmentocbc.network

import com.learning.assignmentocbc.BASE_URL
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun getResponseFromLogin(username: String, password: String) : Response {
    val formBody: RequestBody = FormBody.Builder()
        .add("username",username)
        .add("password",password)
        .build()

    val request : Request = Request.Builder()
        .url(BASE_URL +"login")
        .post(formBody)
        .build()

    return basicCall(request)
}

fun basicCall(request: Request) : Response {
    val client : OkHttpClient = OkHttpClient()

    val call : Call = client.newCall(request)
    return call.execute()
}

fun createApiService() : ApiService{
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    return retrofit.create(ApiService::class.java)
}




//interface APIService {
//    // ...
//
//    @POST("/api/v1/create")
//    suspend fun createEmployee(@Body requestBody: RequestBody): Response<ResponseBody>
//
//    // ...
//}