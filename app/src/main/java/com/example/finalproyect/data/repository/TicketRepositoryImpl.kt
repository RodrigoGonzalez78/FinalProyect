package com.example.finalproyect.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.finalproyect.data.local.dao.TicketDao
import com.example.finalproyect.data.mappers.toPurchaseResult
import com.example.finalproyect.data.mappers.toTicket
import com.example.finalproyect.data.mappers.toTicketEntity
import com.example.finalproyect.data.remote.api.TicketApi
import com.example.finalproyect.domain.model.PurchaseResult
import com.example.finalproyect.domain.model.Ticket
import com.example.finalproyect.domain.repository.TicketRepository
import com.example.finalproyect.utils.PreferenceManager
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class TicketRepositoryImpl @Inject constructor(
    private val ticketApi: TicketApi,
    private val ticketDao: TicketDao,
    private val preferenceManager: PreferenceManager
) : TicketRepository {

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun purchaseTicket(ticketTypeId: Int): Result<PurchaseResult> {
        return try {
            val response = ticketApi.purchaseTicket(ticketTypeId)

            if (response.isSuccessful) {
                response.body()?.let { purchaseResponse ->
                    // Guardar ticket en base de datos local
                    val ticketEntity = purchaseResponse.ticket.toTicketEntity()
                    ticketDao.insertTicket(ticketEntity)

                    // Convertir a modelo de dominio y retornar
                    val purchaseResult = purchaseResponse.toPurchaseResult()
                    Result.success(purchaseResult)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                val errorMessage = when (response.code()) {
                    400 -> "Invalid ticket type ID or missing parameter"
                    401 -> "User not authenticated"
                    404 -> "Ticket type not found"
                    409 -> "Tickets sold out for this type"
                    500 -> "Error processing purchase or generating QR code"
                    else -> "Error: ${response.code()} - ${response.message()}"
                }
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getUserTicketForEvent(eventId: Int): Result<Ticket?> {
        return try {
            val response = ticketApi.getUserTicketForEvent(eventId)

            if (response.isSuccessful) {
                response.body()?.let { ticketDto ->
                    // Guardar en base de datos local
                    val ticketEntity = ticketDto.toTicketEntity()
                    ticketDao.insertTicket(ticketEntity)

                    // Convertir a modelo de dominio y retornar
                    val ticket = ticketDto.toTicket()
                    Result.success(ticket)
                } ?: Result.success(null) // No ticket found
            } else if (response.code() == 404) {
                // No ticket found for this event
                Result.success(null)
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            // En caso de error de red, intentar obtener de base de datos local
            try {
                val currentUserId = preferenceManager.getCurrentUserID().first()
                if (currentUserId != null) {
                    val localTicket = ticketDao.getUserTicketForEvent(eventId, currentUserId.toInt())
                    val ticket = localTicket?.toTicket()
                    Result.success(ticket)
                } else {
                    Result.failure(Exception("User not logged in"))
                }
            } catch (localError: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun getUserTickets(): Result<List<Ticket>> {
        return try {
            val currentUserId = preferenceManager.getCurrentUserID().first().toInt()
            if (currentUserId != null) {
                val ticketEntities = ticketDao.getTicketsByUserId(currentUserId)
                val tickets = ticketEntities.map { it.toTicket() }
                Result.success(tickets)
            } else {
                Result.failure(Exception("User not logged in"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTicketById(ticketId: Int): Result<Ticket?> {
        return try {
            val ticketEntity = ticketDao.getTicketById(ticketId)
            val ticket = ticketEntity?.toTicket()
            Result.success(ticket)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
