package com.example.finalproyect.data.remote.dto

import com.example.finalproyect.data.local.entity.UserEntity
import com.example.finalproyect.domain.model.User

data class UserDto(val id: Int, val name: String, val email: String) {
    fun toEntity() = UserEntity(id, name, email)
    fun toDomain() = User(id, name, email)
}