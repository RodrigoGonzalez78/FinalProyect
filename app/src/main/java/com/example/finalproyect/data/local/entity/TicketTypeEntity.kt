package com.example.finalproyect.data.local.entity


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "ticket_types")
data class TicketTypeEntity(
    @PrimaryKey
    @ColumnInfo(name = "id_ticket_type")
    val id: Int,

    @ColumnInfo(name = "id_event")
    val eventId: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "description")
    val description: String?,

    @ColumnInfo(name = "available")
    val available: Int,

    @ColumnInfo(name = "sold")
    val sold: Int,

    @ColumnInfo(name = "price")
    val price: Double,

    @ColumnInfo(name = "created_at")
    val createdAt: LocalDateTime,

    @ColumnInfo(name = "updated_at")
    val updatedAt: LocalDateTime
)
