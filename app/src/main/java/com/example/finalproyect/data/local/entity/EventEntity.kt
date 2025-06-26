package com.example.finalproyect.data.local.entity

import androidx.room.ColumnInfo
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
    @PrimaryKey
    @ColumnInfo(name = "id_event")
    val id: Long,

    @ColumnInfo(name = "user_id")
    val userId: String,

    @ColumnInfo(name = "locationId", index = true)
    val locationId: Long,

    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,

    val name: String,
    val description: String?,
    val banner: String?,

    @ColumnInfo(name = "is_public")
    val isPublic: Boolean,

    @ColumnInfo(name = "created_at")
    val createdAt: LocalDateTime,

    @ColumnInfo(name = "updated_at")
    val updatedAt: LocalDateTime
)