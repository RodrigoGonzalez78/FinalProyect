package com.example.finalproyect.domain.usecase.ticket


import com.example.finalproyect.domain.model.PurchaseResult
import com.example.finalproyect.domain.repository.TicketRepository
import javax.inject.Inject

class PurchaseTicketUseCase @Inject constructor(
    private val ticketRepository: TicketRepository
) {
    suspend operator fun invoke(ticketTypeId: Int): Result<PurchaseResult> {
        // Validaciones
        if (ticketTypeId <= 0) {
            return Result.failure(Exception("Invalid ticket type ID"))
        }

        return ticketRepository.purchaseTicket(ticketTypeId)
    }
}
