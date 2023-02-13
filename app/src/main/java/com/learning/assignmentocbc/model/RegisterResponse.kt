package com.learning.assignmentocbc.model

data class RegisterResponse(
    val status: String? = null,
    val error: String? = null,
    val token: String? = null,
) {
}