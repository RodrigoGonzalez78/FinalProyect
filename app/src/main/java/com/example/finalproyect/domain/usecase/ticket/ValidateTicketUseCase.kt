package com.example.finalproyect.domain.usecase.ticket

import com.example.finalproyect.domain.model.Ticket
import com.example.finalproyect.domain.repository.TicketRepository


import javax.inject.Inject

class ValidateTicketUseCase @Inject constructor(
    private val ticketRepository: TicketRepository
) {
    suspend operator fun invoke(ticketId: Int): Result<TicketValidationResult> {
        if (ticketId <= 0) {
            return Result.failure(Exception("Invalid ticket ID"))
        }

        return try {
            val ticketResult = ticketRepository.getTicketById(ticketId)

            if (ticketResult.isFailure) {
                return Result.failure(ticketResult.exceptionOrNull() ?: Exception("Failed to get ticket"))
            }

            val ticket = ticketResult.getOrNull()

            if (ticket == null) {
                return Result.success(TicketValidationResult(
                    isValid = false,
                    message = "Ticket not found",
                    ticket = null
                ))
            }

            val isValid = ticket.isValid
            val message = when {
                !ticket.hasQrCode -> "Ticket has no QR code"
                ticket.entryNumber.isBlank() -> "Ticket has no entry number"
                isValid -> "Ticket is valid"
                else -> "Ticket is invalid"
            }

            Result.success(TicketValidationResult(
                isValid = isValid,
                message = message,
                ticket = ticket
            ))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

data class TicketValidationResult(
    val isValid: Boolean,
    val message: String,
    val ticket: Ticket?
)
