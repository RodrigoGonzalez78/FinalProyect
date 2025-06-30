package com.example.finalproyect.data.repository


import android.os.Build
import androidx.annotation.RequiresApi
import com.example.finalproyect.data.mappers.toScanResult
import com.example.finalproyect.data.remote.api.TicketScanApi
import com.example.finalproyect.data.remote.dto.request.ScanTicketRequest
import com.example.finalproyect.domain.model.ScanResult
import com.example.finalproyect.domain.repository.TicketScanRepository
import javax.inject.Inject

class TicketScanRepositoryImpl @Inject constructor(
    private val ticketScanApi: TicketScanApi
) : TicketScanRepository {

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun scanTicket(qrCode: String, eventId: Int): Result<ScanResult> {
        return try {
            val request = ScanTicketRequest(qrCode, eventId)
            val response = ticketScanApi.scanTicket(request)

            if (response.isSuccessful) {
                response.body()?.let { scanResponse ->
                    val scanResult = scanResponse.toScanResult()
                    Result.success(scanResult)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                val errorMessage = when (response.code()) {
                    400 -> "QR code inválido o malformado"
                    401 -> "Usuario no autenticado"
                    403 -> "Ticket alterado, inválido o no pertenece al evento"
                    404 -> "Ticket no encontrado"
                    500 -> "Error interno del servidor"
                    else -> "Error: ${response.code()} - ${response.message()}"
                }
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
