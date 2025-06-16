package com.example.finalproyect.domain.usecase.ticket_type


import com.example.finalproyect.domain.model.TicketType
import com.example.finalproyect.domain.repository.TicketTypeRepository
import javax.inject.Inject

class CreateTicketTypeUseCase @Inject constructor(
    private val ticketTypeRepository: TicketTypeRepository
) {
    suspend operator fun invoke(
        eventId: Int,
        name: String,
        description: String?,
        available: Int,
        price: Double
    ): Result<TicketType> {
        // Validaciones
        if (eventId <= 0) {
            return Result.failure(Exception("Invalid event ID"))
        }
        if (name.isBlank()) {
            return Result.failure(Exception("Ticket type name cannot be empty"))
        }
        if (available <= 0) {
            return Result.failure(Exception("Available tickets must be greater than 0"))
        }
        if (price < 0) {
            return Result.failure(Exception("Price cannot be negative"))
        }

        return ticketTypeRepository.createTicketType(
            eventId = eventId,
            name = name,
            description = description,
            available = available,
            price = price
        )
    }
}
