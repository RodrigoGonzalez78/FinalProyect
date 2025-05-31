package com.example.finalproyect.data.remote.api

import com.example.finalproyect.data.remote.dto.request.LoginRequest
import com.example.finalproyect.data.remote.dto.request.RegisterRequest
import com.example.finalproyect.data.remote.dto.response.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): LoginResponse
}