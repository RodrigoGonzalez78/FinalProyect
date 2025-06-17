package com.example.finalproyect.domain.usecase.user

import com.example.finalproyect.domain.model.User
import com.example.finalproyect.domain.repository.UserRepository
import java.time.LocalDate
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        userId: Int,
        name: String,
        lastName: String,
        birthday: LocalDate,
        phone: String
    ): Result<User> {
        // Validaciones
        if (userId <= 0) {
            return Result.failure(Exception("Invalid user ID"))
        }
        if (name.isBlank()) {
            return Result.failure(Exception("Name cannot be empty"))
        }
        if (lastName.isBlank()) {
            return Result.failure(Exception("Last name cannot be empty"))
        }
        if (phone.isBlank()) {
            return Result.failure(Exception("Phone cannot be empty"))
        }
        if (birthday.isAfter(LocalDate.now().minusYears(18))) {
            return Result.failure(Exception("User must be at least 18 years old"))
        }

        return userRepository.updateUser(
            userId = userId,
            name = name,
            lastName = lastName,
            birthday = birthday,
            phone = phone
        )
    }
}
