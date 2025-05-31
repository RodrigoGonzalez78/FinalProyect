package com.example.finalproyect.data.remote.api

import com.example.finalproyect.data.remote.dto.response.ImageUploadResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UploadApi {
    @Multipart
    @POST("uploads/image")
    suspend fun uploadImage(@Part image: MultipartBody.Part): ImageUploadResponse
}