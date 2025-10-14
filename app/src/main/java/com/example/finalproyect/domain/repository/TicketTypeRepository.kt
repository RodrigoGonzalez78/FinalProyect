package com.example.finalproyect.domain.repository

import com.example.finalproyect.domain.model.TicketType


interface TicketTypeRepository {
    suspend fun createTicketType(
        eventId: Int,
        name: String,
        description: String?,
        available: Int,
        price: Double
    ): Result<TicketType>

    suspend fun getTicketTypesByEvent(eventId: Int): Result<List<TicketType>>

    suspend fun getTicketTypeById(ticketTypeId: Int): Result<TicketType>

    suspend fun updateTicketType(
        eventId: Int,
        ticketTypeId: Int,
        name: String,
        description: String?,
        available: Int,
        price: Double
    ): Result<TicketType>

    suspend fun deleteTicketType(eventId: Int, ticketTypeId: Int): Result<Unit>
}
