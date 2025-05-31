package com.example.finalproyect.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Entity(
    tableName = "events",
    foreignKeys = [
        ForeignKey(
            entity = LocationEntity::class,
            parentColumns = ["id"],
            childColumns = ["locationId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class EventEntity(
    @PrimaryKey val id: Long,
    val locationId: Long,
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val name: String,
    val description: String?,
    val banner: String?,
    val isPublic: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
