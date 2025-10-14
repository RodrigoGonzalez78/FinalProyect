package com.example.finalproyect.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.finalproyect.data.local.dao.TicketTypeDao
import com.example.finalproyect.data.mappers.toTicketType
import com.example.finalproyect.data.mappers.toTicketTypeEntity
import com.example.finalproyect.data.remote.api.TicketTypeApi
import com.example.finalproyect.data.remote.dto.request.CreateTicketTypeRequest
import com.example.finalproyect.data.remote.dto.request.UpdateTicketTypeRequest
import com.example.finalproyect.domain.model.TicketType
import com.example.finalproyect.domain.repository.TicketTypeRepository


import javax.inject.Inject

class TicketTypeRepositoryImpl @Inject constructor(
    private val ticketTypeApi: TicketTypeApi,
    private val ticketTypeDao: TicketTypeDao
) : TicketTypeRepository {

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun createTicketType(
        eventId: Int,
        name: String,
        description: String?,
        available: Int,
        price: Double
    ): Result<TicketType> {
        return try {
            val request = CreateTicketTypeRequest(
                name = name,
                description = description,
                available = available,
                price = price
            )

            val response = ticketTypeApi.createTicketType(eventId, request)

            if (response.isSuccessful) {
                response.body()?.let { ticketTypeDto ->
                    // Guardar en base de datos local
                    val ticketTypeEntity = ticketTypeDto.toTicketTypeEntity()
                    ticketTypeDao.insertTicketType(ticketTypeEntity)

                    // Convertir a modelo de dominio y retornar
                    val ticketType = ticketTypeDto.toTicketType()
                    Result.success(ticketType)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getTicketTypesByEvent(eventId: Int): Result<List<TicketType>> {
        return try {
            val response = ticketTypeApi.getTicketTypesByEvent(eventId)

            if (response.isSuccessful) {
                response.body()?.let { ticketTypeDtos ->
                    // Guardar en base de datos local
                    val ticketTypeEntities = ticketTypeDtos.map { it.toTicketTypeEntity() }
                    ticketTypeDao.insertTicketTypes(ticketTypeEntities)

                    // Convertir a modelos de dominio y retornar
                    val ticketTypes = ticketTypeDtos.map { it.toTicketType() }
                    Result.success(ticketTypes)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            // En caso de error de red, intentar obtener de base de datos local
            try {
                val localTicketTypes = ticketTypeDao.getTicketTypesByEventId(eventId)
                val ticketTypes = localTicketTypes.map { it.toTicketType() }
                Result.success(ticketTypes)
            } catch (localError: Exception) {
                Result.failure(e)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun updateTicketType(
        eventId: Int,
        ticketTypeId: Int,
        name: String,
        description: String?,
        available: Int,
        price: Double
    ): Result<TicketType> {
        return try {
            val request = UpdateTicketTypeRequest(
                name = name,
                description = description,
                available = available,
                price = price
            )

            val response = ticketTypeApi.updateTicketType(eventId, ticketTypeId, request)

            if (response.isSuccessful) {
                response.body()?.let { ticketTypeDto ->
                    // Actualizar en base de datos local
                    val ticketTypeEntity = ticketTypeDto.toTicketTypeEntity()
                    ticketTypeDao.updateTicketType(ticketTypeEntity)

                    // Convertir a modelo de dominio y retornar
                    val ticketType = ticketTypeDto.toTicketType()
                    Result.success(ticketType)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteTicketType(eventId: Int, ticketTypeId: Int): Result<Unit> {
        return try {
            val response = ticketTypeApi.deleteTicketType(eventId, ticketTypeId)

            if (response.isSuccessful) {
                // Eliminar de base de datos local
                ticketTypeDao.deleteTicketTypeById(ticketTypeId)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getTicketTypeById(ticketTypeId: Int): Result<TicketType> {
        return try {
            val response = ticketTypeApi.getTicketTypeById(ticketTypeId)

            if (response.isSuccessful) {
                response.body()?.let { ticketTypeDto ->
                    // Guardar en base de datos local
                    val ticketTypeEntity = ticketTypeDto.toTicketTypeEntity()
                    ticketTypeDao.insertTicketType(ticketTypeEntity)

                    // Convertir a modelo de dominio y retornar
                    val ticketType = ticketTypeDto.toTicketType()
                    Result.success(ticketType)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            // En caso de error de red, intentar obtener de base de datos local
            try {
                val localTicketType = ticketTypeDao.getTicketTypeById(ticketTypeId)
                localTicketType?.let {
                    Result.success(it.toTicketType())
                } ?: Result.failure(Exception("TicketType no encontrado localmente"))
            } catch (localError: Exception) {
                Result.failure(e)
            }
        }
    }
}
