package com.example.finalproyect.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.finalproyect.data.local.dao.OrganizerDao
import com.example.finalproyect.data.mappers.toOrganizer
import com.example.finalproyect.data.mappers.toOrganizerEntity
import com.example.finalproyect.data.remote.api.OrganizerApi
import com.example.finalproyect.data.remote.dto.request.CreateOrganizerRequest
import com.example.finalproyect.data.remote.dto.request.UpdateOrganizerRequest
import com.example.finalproyect.domain.model.Organizer
import com.example.finalproyect.domain.repository.OrganizerRepository
import javax.inject.Inject

class OrganizerRepositoryImpl @Inject constructor(
    private val organizerApi: OrganizerApi,
    private val organizerDao: OrganizerDao
) : OrganizerRepository {

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun createOrganizer(
        eventId: Int,
        userId: Int,
        roleId: Int
    ): Result<Organizer> {
        return try {
            val request = CreateOrganizerRequest(
                idUser = userId,
                idRol = roleId
            )

            val response = organizerApi.createOrganizer(eventId, request)

            if (response.isSuccessful) {
                response.body()?.let { organizerDto ->
                    // Guardar en base de datos local
                    val organizerEntity = organizerDto.toOrganizerEntity()
                    organizerDao.insertOrganizer(organizerEntity)

                    // Convertir a modelo de dominio y retornar
                    val organizer = organizerDto.toOrganizer()
                    Result.success(organizer)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                val errorMessage = when (response.code()) {
                    400 -> "Invalid data or attempt to assign main admin role"
                    403 -> "Insufficient permissions to add organizers"
                    else -> "Error: ${response.code()} - ${response.message()}"
                }
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getOrganizersByEvent(eventId: Int): Result<List<Organizer>> {
        return try {
            val response = organizerApi.getOrganizersByEvent(eventId)

            if (response.isSuccessful) {
                response.body()?.let { organizerDtos ->
                    // Guardar en base de datos local
                    val organizerEntities = organizerDtos.map { it.toOrganizerEntity() }
                    organizerDao.insertOrganizers(organizerEntities)

                    // Convertir a modelos de dominio y retornar
                    val organizers = organizerDtos.map { it.toOrganizer() }
                    Result.success(organizers)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            // En caso de error de red, intentar obtener de base de datos local
            try {
                val localOrganizers = organizerDao.getOrganizersByEventId(eventId)
                val organizers = localOrganizers.map { it.toOrganizer() }
                Result.success(organizers)
            } catch (localError: Exception) {
                Result.failure(e)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun updateOrganizerRole(
        eventId: Int,
        organizerId: Int,
        newRoleId: Int
    ): Result<Organizer> {
        return try {
            val request = UpdateOrganizerRequest(idRol = newRoleId)

            val response = organizerApi.updateOrganizer(eventId, organizerId, request)

            if (response.isSuccessful) {
                response.body()?.let { organizerDto ->
                    // Actualizar en base de datos local
                    val organizerEntity = organizerDto.toOrganizerEntity()
                    organizerDao.updateOrganizer(organizerEntity)

                    // Convertir a modelo de dominio y retornar
                    val organizer = organizerDto.toOrganizer()
                    Result.success(organizer)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                val errorMessage = when (response.code()) {
                    400 -> "Invalid data or attempt to assign main admin role"
                    403 -> "Insufficient permissions to update organizer"
                    else -> "Error: ${response.code()} - ${response.message()}"
                }
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteOrganizer(eventId: Int, organizerId: Int): Result<Unit> {
        return try {
            val response = organizerApi.deleteOrganizer(eventId, organizerId)

            if (response.isSuccessful) {
                // Eliminar de base de datos local
                organizerDao.deleteOrganizerById(organizerId)
                Result.success(Unit)
            } else {
                val errorMessage = when (response.code()) {
                    403 -> "Cannot delete main admin organizer"
                    404 -> "Organizer not found"
                    else -> "Error: ${response.code()} - ${response.message()}"
                }
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getOrganizerById(organizerId: Int): Result<Organizer?> {
        return try {
            val organizerEntity = organizerDao.getOrganizerById(organizerId)
            val organizer = organizerEntity?.toOrganizer()
            Result.success(organizer)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun isUserOrganizerOfEvent(eventId: Int, userId: Int): Result<Boolean> {
        return try {
            val organizerEntity = organizerDao.getOrganizerByEventAndUser(eventId, userId)
            Result.success(organizerEntity != null)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
