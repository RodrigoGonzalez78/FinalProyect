package com.example.finalproyect.domain.repository

import com.example.finalproyect.domain.model.PurchaseResult
import com.example.finalproyect.domain.model.Ticket


interface TicketRepository {
    suspend fun purchaseTicket(ticketTypeId: Int): Result<PurchaseResult>

    suspend fun getUserTicketForEvent(eventId: Int): Result<Ticket?>

    suspend fun getUserTickets(): Result<List<Ticket>>

    suspend fun getTicketById(ticketId: Int): Result<Ticket?>
}
