package com.example.finalproyect.domain.usecase.user

import com.example.finalproyect.domain.model.User
import com.example.finalproyect.domain.repository.UserRepository
import javax.inject.Inject

class GetUserByIdUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: Int): Result<User> {
        if (userId <= 0) {
            return Result.failure(Exception("Invalid user ID"))
        }

        return userRepository.getUserById(userId)
    }
}
