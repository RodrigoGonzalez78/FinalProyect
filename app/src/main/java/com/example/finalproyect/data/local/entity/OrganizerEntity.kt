package com.example.finalproyect.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "organizers")
data class OrganizerEntity(
    @PrimaryKey
    @ColumnInfo(name = "id_organizer")
    val idOrganizer: Int,

    @ColumnInfo(name = "id_event")
    val idEvent: Int,

    @ColumnInfo(name = "id_rol")
    val idRol: Int,

    @ColumnInfo(name = "id_user")
    val idUser: Int,

    @ColumnInfo(name = "created_at")
    val createdAt: LocalDateTime,

    @ColumnInfo(name = "updated_at")
    val updatedAt: LocalDateTime,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "last_name")
    val lastName: String
)
