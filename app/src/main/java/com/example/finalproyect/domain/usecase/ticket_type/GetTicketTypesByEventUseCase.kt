package com.example.finalproyect.domain.usecase.ticket_type

import com.example.finalproyect.domain.model.TicketType
import com.example.finalproyect.domain.repository.TicketTypeRepository
import javax.inject.Inject

class GetTicketTypesByEventUseCase @Inject constructor(
    private val ticketTypeRepository: TicketTypeRepository
) {
    suspend operator fun invoke(eventId: Int): Result<List<TicketType>> {
        if (eventId <= 0) {
            return Result.failure(Exception("Invalid event ID"))
        }

        return ticketTypeRepository.getTicketTypesByEvent(eventId)
    }
}
