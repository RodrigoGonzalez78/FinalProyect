package com.example.finalproyect.data.repository

import android.content.Context
import android.net.Uri
import com.example.finalproyect.data.remote.api.UploadApi
import com.example.finalproyect.domain.repository.UploadRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class UploadRepositoryImpl @Inject constructor(
    private val uploadApi: UploadApi,
    @ApplicationContext private val context: Context
) : UploadRepository {

    override suspend fun uploadImage(imageUri: Uri): Result<String> {
        return try {
            val file = uriToFile(imageUri)
            uploadImage(file)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun uploadImage(imageFile: File): Result<String> {
        return try {
            val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)

            val response = uploadApi.uploadImage(body)
            Result.success(response.filename)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun uriToFile(uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)
        val tempFile = File.createTempFile("upload", ".jpg", context.cacheDir)
        tempFile.deleteOnExit()

        FileOutputStream(tempFile).use { outputStream ->
            inputStream?.copyTo(outputStream)
        }

        return tempFile
    }
}