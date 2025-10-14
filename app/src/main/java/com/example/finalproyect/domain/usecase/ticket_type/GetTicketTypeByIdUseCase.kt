package com.example.finalproyect.domain.usecase.ticket_type

import com.example.finalproyect.domain.model.TicketType
import com.example.finalproyect.domain.repository.TicketTypeRepository
import javax.inject.Inject

class GetTicketTypeByIdUseCase @Inject constructor(
    private val ticketTypeRepository: TicketTypeRepository
) {
    suspend operator fun invoke(ticketTypeId: Int): Result<TicketType> {
        if (ticketTypeId <= 0) {
            return Result.failure(IllegalArgumentException("ID de tipo de ticket inválido"))
        }

        return ticketTypeRepository.getTicketTypeById(ticketTypeId)
    }
}