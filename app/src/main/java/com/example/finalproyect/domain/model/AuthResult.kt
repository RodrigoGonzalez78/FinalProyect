package com.example.finalproyect.domain.model

data class AuthResult(
    val token: String,
    val name: String,
    val lastName: String,
    val phone: String,
    val email: String,
    val isSuccess: Boolean = true,
    val errorMessage: String? = null
)