package com.learning.assignmentocbc.model

data class LoginResponse (

    val status : String,
    val token : String ,
    val username : String ,
    val accountNo : String


) {

    override fun toString(): String {
        return "token: $token \nusername: $username\naccount no: $accountNo"
    }
}

