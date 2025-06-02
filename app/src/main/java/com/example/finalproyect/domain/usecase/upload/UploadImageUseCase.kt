package com.example.finalproyect.domain.usecase.upload

import android.net.Uri
import com.example.finalproyect.domain.repository.UploadRepository
import javax.inject.Inject

class UploadImageUseCase @Inject constructor(
    private val uploadRepository: UploadRepository
) {
    suspend operator fun invoke(imageUri: Uri): Result<String> {
        return uploadRepository.uploadImage(imageUri)
    }
}