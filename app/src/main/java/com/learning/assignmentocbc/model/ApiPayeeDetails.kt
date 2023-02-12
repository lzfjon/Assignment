package com.learning.assignmentocbc.model

import com.google.gson.annotations.SerializedName

data class ApiPayeeDetails(

    val status : String? = null,
    val data : List<PayeeDetails> ? = null,

) {
}

data class PayeeDetails(

    val id : String? = null,
    @SerializedName("name")
    val payeeName : String? = null,
    @SerializedName("accountNo")
    val accountNumber : String? = null,

) {
}