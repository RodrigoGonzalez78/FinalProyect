package com.example.finalproyect.domain.repository

import com.example.finalproyect.domain.model.ScanResult

interface TicketScanRepository {
    suspend fun scanTicket(qrCode: String, eventId: Int): Result<ScanResult>
}