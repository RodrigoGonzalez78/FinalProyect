package com.example.finalproyect.domain.usecase.ticket


import com.example.finalproyect.domain.model.Ticket
import com.example.finalproyect.domain.repository.TicketRepository
import javax.inject.Inject

class GetUserTicketForEventUseCase @Inject constructor(
    private val ticketRepository: TicketRepository
) {
    suspend operator fun invoke(eventId: Int): Result<Ticket?> {
        if (eventId <= 0) {
            return Result.failure(Exception("Invalid event ID"))
        }

        return ticketRepository.getUserTicketForEvent(eventId)
    }
}
