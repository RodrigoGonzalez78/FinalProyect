package com.example.finalproyect.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "locations")
data class LocationEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val direction: String,
    val latitude: Double,
    val longitude: Double,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)