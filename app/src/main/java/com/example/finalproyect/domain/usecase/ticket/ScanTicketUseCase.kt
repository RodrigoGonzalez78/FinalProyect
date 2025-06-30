package com.example.finalproyect.domain.usecase.ticket


import com.example.finalproyect.domain.model.ScanResult
import com.example.finalproyect.domain.repository.TicketScanRepository
import javax.inject.Inject

class ScanTicketUseCase @Inject constructor(
    private val ticketScanRepository: TicketScanRepository
) {
    suspend operator fun invoke(qrCode: String, eventId: Int): Result<ScanResult> {
        // Validar que el QR code no esté vacío
        if (qrCode.isBlank()) {
            return Result.failure(Exception("El código QR no puede estar vacío"))
        }

        // Validar que el event ID sea válido
        if (eventId <= 0) {
            return Result.failure(Exception("ID de evento inválido"))
        }

        return ticketScanRepository.scanTicket(qrCode, eventId)
    }
}
