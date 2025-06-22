package com.example.finalproyect.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "organizers")
data class OrganizerEntity(
    @PrimaryKey
    @ColumnInfo(name = "id_organizer")
    val id: Int,

    @ColumnInfo(name = "id_event")
    val eventId: Int,

    @ColumnInfo(name = "id_rol")
    val roleId: Int,

    @ColumnInfo(name = "id_user")
    val userId: Int,

    @ColumnInfo(name = "created_at")
    val createdAt: LocalDateTime,

    @ColumnInfo(name = "updated_at")
    val updatedAt: LocalDateTime,

    @ColumnInfo(name = "name")
    val name: String? = null,

    @ColumnInfo(name = "last_name")
    val lastName: String? = null
)
