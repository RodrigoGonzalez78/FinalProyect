package com.example.finalproyect.domain.usecase.ticket_type


import com.example.finalproyect.domain.repository.TicketTypeRepository
import javax.inject.Inject

class DeleteTicketTypeUseCase @Inject constructor(
    private val ticketTypeRepository: TicketTypeRepository
) {
    suspend operator fun invoke(eventId: Int, ticketTypeId: Int): Result<Unit> {
        if (eventId <= 0) {
            return Result.failure(Exception("Invalid event ID"))
        }
        if (ticketTypeId <= 0) {
            return Result.failure(Exception("Invalid ticket type ID"))
        }

        return ticketTypeRepository.deleteTicketType(eventId, ticketTypeId)
    }
}
