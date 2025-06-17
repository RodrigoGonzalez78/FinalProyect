package com.example.finalproyect.domain.usecase.user

import com.example.finalproyect.domain.repository.UserRepository
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        userId: Int,
        oldPassword: String,
        newPassword: String
    ): Result<Unit> {
        // Validaciones
        if (userId <= 0) {
            return Result.failure(Exception("Invalid user ID"))
        }
        if (oldPassword.isBlank()) {
            return Result.failure(Exception("Old password cannot be empty"))
        }
        if (newPassword.isBlank()) {
            return Result.failure(Exception("New password cannot be empty"))
        }
        if (newPassword.length < 6) {
            return Result.failure(Exception("New password must be at least 6 characters long"))
        }
        if (oldPassword == newPassword) {
            return Result.failure(Exception("New password must be different from old password"))
        }

        return userRepository.changePassword(
            userId = userId,
            oldPassword = oldPassword,
            newPassword = newPassword
        )
    }
}
