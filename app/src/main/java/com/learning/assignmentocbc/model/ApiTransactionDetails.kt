package com.learning.assignmentocbc.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.Date

data class ApiTransactionDetails(

    val status : String? = null,
    val data : List<TransactionDetails>? = null
) : Serializable{
}



data class TransactionDetails(

    val transactionId : String? = null ,
    @SerializedName("amount")
    val transactionAmount : Double? = null,
    val transactionDate : Date? = null,
    @SerializedName("description")
    val transactionDescription : String? = null,
    @SerializedName("receipient")
    val transactionReceipient : Receipent? = null
) : Serializable{
}

data class Receipent(
   val accountNo : String? ,
   val accountHolder : String?

) : Serializable{
}