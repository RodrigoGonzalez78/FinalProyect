package com.example.finalproyect.domain.repository

import android.net.Uri
import java.io.File

interface UploadRepository {
    suspend fun uploadImage(imageUri: Uri): Result<String>
    suspend fun uploadImage(imageFile: File): Result<String>
}