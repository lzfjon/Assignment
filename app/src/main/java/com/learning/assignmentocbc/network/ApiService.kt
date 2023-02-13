package com.learning.assignmentocbc.network

import com.learning.assignmentocbc.model.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {


    @POST("login")
    suspend fun postLoginRequest(@Body loginRequest: LoginRequest) : Response<LoginResponse>

    @GET("payees")
    suspend fun getPayeesDetails(@Header("Authorization") token: String): Response<ApiPayeeDetails>

    @GET("transactions")
    suspend fun getTransactionDetails(@Header("Authorization") token: String): Response<ApiTransactionDetails>

    @GET("balance")
    suspend fun getBalanceDetails(@Header("Authorization") token: String): Response<AccountBalance>

    @POST("transfer")
    suspend fun postTransfer(@Header("Authorization") token: String, @Body transferInfo: TransferRequest):Response<TransferResponse>

//    @POST("register")
//    suspend fun postRegister(@Body registerInfo: ArrayList<String> ): Response<ArrayList<String>>
    @POST("register")
    suspend fun postRegister(@Body registerRequest : RegisterRequest ): Response<RegisterResponse>






}
