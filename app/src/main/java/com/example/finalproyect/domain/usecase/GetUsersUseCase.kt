package com.example.finalproyect.domain.usecase

import com.example.finalproyect.domain.model.User
import com.example.finalproyect.domain.repository.UserRepository
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val repo: UserRepository
) {
    suspend operator fun invoke(): List<User> = repo.getUsers()
}