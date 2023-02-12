package com.learning.assignmentocbc.model

data class TransferRequest(

    val receipientAccountNo : String,
    val amount : Double,
    val description: String,

    ) {
}
