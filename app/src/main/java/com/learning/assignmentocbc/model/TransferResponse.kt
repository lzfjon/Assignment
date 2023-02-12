package com.learning.assignmentocbc.model

data class TransferResponse(

    val status : String,
    val transactionId : String,
    val transferRequest : TransferRequest

){

}