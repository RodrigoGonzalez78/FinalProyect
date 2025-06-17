package com.example.finalproyect.data.remote.api

import com.example.finalproyect.data.remote.dto.UserDto
import com.example.finalproyect.data.remote.dto.request.ChangePasswordRequest
import com.example.finalproyect.data.remote.dto.request.UpdateUserRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApi {

    @GET("users/{id}")
    suspend fun getUserById(
        @Path("id") userId: Int
    ): Response<UserDto>

    @PUT("users/{id}")
    suspend fun updateUser(
        @Path("id") userId: Int,
        @Body request: UpdateUserRequest
    ): Response<UserDto>

    @PUT("users/{id}/password")
    suspend fun changePassword(
        @Path("id") userId: Int,
        @Body request: ChangePasswordRequest
    ): Response<Unit>
}
