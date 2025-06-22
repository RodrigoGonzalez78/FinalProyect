package com.example.finalproyect.domain.usecase.ticket


import com.example.finalproyect.domain.model.Ticket
import com.example.finalproyect.domain.repository.TicketRepository
import javax.inject.Inject

class GetUserTicketsUseCase @Inject constructor(
    private val ticketRepository: TicketRepository
) {
    suspend operator fun invoke(): Result<List<Ticket>> {
        return ticketRepository.getUserTickets()
    }
}
