package com.example.finalproyect.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "tickets")
data class TicketEntity(
    @PrimaryKey
    @ColumnInfo(name = "id_ticket")
    val id: Int,

    @ColumnInfo(name = "id_ticket_type")
    val ticketTypeId: Int,

    @ColumnInfo(name = "id_user")
    val userId: Int,

    @ColumnInfo(name = "qr_code")
    val qrCode: String?,

    @ColumnInfo(name = "price")
    val price: Double,

    @ColumnInfo(name = "entry_number")
    val entryNumber: String,

    @ColumnInfo(name = "created_at")
    val createdAt: LocalDateTime,

    @ColumnInfo(name = "updated_at")
    val updatedAt: LocalDateTime
)
