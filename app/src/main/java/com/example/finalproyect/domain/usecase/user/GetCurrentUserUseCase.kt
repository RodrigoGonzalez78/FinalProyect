package com.example.finalproyect.domain.usecase.user



import com.example.finalproyect.domain.model.User
import com.example.finalproyect.domain.repository.UserRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<User?> {
        return userRepository.getCurrentUser()
    }
}