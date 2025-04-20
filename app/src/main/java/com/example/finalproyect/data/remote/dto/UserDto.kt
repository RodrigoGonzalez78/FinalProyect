package com.example.finalproyect.data.remote.dto

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.finalproyect.data.local.entity.UserEntity
import com.example.finalproyect.domain.model.User
import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.time.LocalDateTime

data class UserDto(
    @SerializedName("id_user")
    val idUser: Long,

    @SerializedName("name")
    val name: String,

    @SerializedName("last_name")
    val lastName: String,

    @SerializedName("birthday")
    val birthday: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("phone")
    val phone: String?,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String,

    @SerializedName("deleted_at")
    val deletedAt: String?
) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun toEntity() = UserEntity(
        idUser = idUser,
        name = name,
        lastName = lastName,
        birthday = LocalDate.parse(birthday),
        email = email,
        phone = phone,
        createdAt = LocalDateTime.parse(createdAt),
        updatedAt = LocalDateTime.parse(updatedAt),
        deletedAt = deletedAt?.let { LocalDateTime.parse(it) }
    )

    @RequiresApi(Build.VERSION_CODES.O)
    fun toDomain() = User(
        id = idUser,
        name = name,
        lastName = lastName,
        birthday = LocalDate.parse(birthday),
        email = email,
        phone = phone,
        createdAt = LocalDateTime.parse(createdAt),
        updatedAt = LocalDateTime.parse(updatedAt),
        deletedAt = deletedAt?.let { LocalDateTime.parse(it) }
    )

    companion object {
        fun fromDomain(user: User): UserDto {
            return UserDto(
                idUser = user.id,
                name = user.name,
                lastName = user.lastName,
                birthday = user.birthday.toString(),
                email = user.email,
                phone = user.phone,
                createdAt = user.createdAt.toString(),
                updatedAt = user.updatedAt.toString(),
                deletedAt = user.deletedAt?.toString()
            )
        }
    }
}