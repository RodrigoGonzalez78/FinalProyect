package com.example.finalproyect.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class ImageUploadResponse(
    @SerializedName("filename") val filename: String
)