package com.example.finalproyect.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.finalproyect.data.local.dao.EventDao
import com.example.finalproyect.data.local.dao.LocationDao
import com.example.finalproyect.data.local.entity.EventEntity
import com.example.finalproyect.data.local.entity.LocationEntity
import com.example.finalproyect.data.mappers.parseIsoDate
import com.example.finalproyect.data.mappers.parseIsoDateTime
import com.example.finalproyect.data.mappers.parseIsoTime
import com.example.finalproyect.data.mappers.toEvent
import com.example.finalproyect.data.mappers.toEventDetail
import com.example.finalproyect.data.mappers.toEventEntity
import com.example.finalproyect.data.mappers.toLocationEntity
import com.example.finalproyect.data.mappers.toPaginatedEvents
import com.example.finalproyect.data.remote.api.EventApi
import com.example.finalproyect.data.remote.dto.request.CreateEventRequest
import com.example.finalproyect.data.remote.dto.request.EventRequest
import com.example.finalproyect.data.remote.dto.request.LocationRequest
import com.example.finalproyect.data.remote.dto.request.UpdateEventRequest
import com.example.finalproyect.domain.model.Event
import com.example.finalproyect.domain.model.EventDetail
import com.example.finalproyect.domain.model.Location
import com.example.finalproyect.domain.model.PaginatedEvents
import com.example.finalproyect.domain.repository.EventRepository
import com.example.finalproyect.utils.PreferenceManager
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val eventApi: EventApi,
    private val preferenceManager: PreferenceManager,
    private val locationDao: LocationDao,
    private val eventDao: EventDao
) : EventRepository {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun searchPublicEvents(
        name: String,
        page: Int,
        size: Int
    ): Result<PaginatedEvents> {
        return try {
            val response = eventApi.searchPublicEvents(name, page, size)
            if (response.isSuccessful) {
                response.body()?.let { paginatedResponse ->
                    val paginatedEvents = paginatedResponse.toPaginatedEvents()
                    Result.success(paginatedEvents)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getUserEvents(
        page: Int,
        size: Int
    ): Result<PaginatedEvents> {
        return try {
            val response = eventApi.getUserEvents(page, size)
            if (response.isSuccessful) {
                response.body()?.let { paginatedResponse ->
                    val paginatedEvents = paginatedResponse.toPaginatedEvents()
                    Result.success(paginatedEvents)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getEventById(eventId: Int): Result<EventDetail> {
        return try {
            val response = eventApi.getEventById(eventId)
            if (response.isSuccessful) {
                response.body()?.let { eventDetailResponse ->
                    val eventDetail = eventDetailResponse.toEventDetail()
                    Result.success(eventDetail)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun updateEvent(
        eventId: Int,
        name: String,
        description: String?,
        date: LocalDateTime,
        startTime: LocalDateTime,
        endTime: LocalDateTime,
        banner: String?,
        locationId: Int,
        isPublic: Boolean
    ): Result<Event> {
        return try {
            val request = UpdateEventRequest(
                name = name,
                description = description,
                date = date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "Z",
                startTime = startTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "Z",
                endTime = endTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "Z",
                banner = banner,
                idLocation = locationId,
                isPublic = isPublic
            )

            val response = eventApi.updateEvent(eventId, request)
            if (response.isSuccessful) {
                response.body()?.let { eventDto ->
                    val event = eventDto.toEvent()
                    Result.success(event)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteEvent(eventId: Int): Result<Unit> {
        return try {
            val response = eventApi.deleteEvent(eventId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun createEvent(
        name: String,
        description: String,
        date: String,
        startTime: String,
        endTime: String,
        banner: String,
        isPublic: Boolean,
        locationName: String,
        locationDirection: String,
        locationLatitude: Double,
        locationLongitude: Double
    ): Result<Event> {
        return try {
            val request = CreateEventRequest(
                event = EventRequest(
                    name = name,
                    description = description,
                    date = date,
                    banner = banner,
                    startTime = startTime,
                    endTime = endTime,
                    isPublic = isPublic
                ),
                location = LocationRequest(
                    name = locationName,
                    direction = locationDirection,
                    latitude = locationLatitude,
                    longitude = locationLongitude
                )
            )

            val response = eventApi.createEvent(request)

            if (response.isSuccessful) {
                response.body()?.let { createEventResponse ->
                    // Obtener el userId del usuario autenticado
                    val currentUserId = preferenceManager.getCurrentUserEmail()
                        ?: throw Exception("User not authenticated")

                    // Crear y guardar la ubicación en la base de datos local
                    val locationEntity = LocationEntity(
                        id = createEventResponse.idLocation,
                        name = locationName,
                        direction = locationDirection,
                        latitude = locationLatitude,
                        longitude = locationLongitude,
                        createdAt = createEventResponse.createdAt.parseIsoDateTime(),
                        updatedAt = createEventResponse.updatedAt.parseIsoDateTime()
                    )
                    locationDao.insertLocation(locationEntity)

                    // Crear y guardar el evento en la base de datos local
                    val eventEntity = EventEntity(
                        id = createEventResponse.idEvent,
                        locationId = createEventResponse.idLocation,
                        date = date.parseIsoDate(),
                        startTime = startTime.parseIsoTime(),
                        endTime = endTime.parseIsoTime(),
                        name = name,
                        description = description,
                        banner = banner,
                        isPublic = isPublic,
                        createdAt = createEventResponse.createdAt.parseIsoDateTime(),
                        updatedAt = createEventResponse.updatedAt.parseIsoDateTime(),
                        userId = currentUserId.toString()
                    )
                    eventDao.insertEvent(eventEntity)

                    // Crear el objeto Location para el Event
                    val location = Location(
                        id = createEventResponse.idLocation,
                        name = locationName,
                        direction = locationDirection,
                        latitude = locationLatitude,
                        longitude = locationLongitude,
                        createdAt = createEventResponse.createdAt.parseIsoDateTime(),
                        updatedAt = createEventResponse.updatedAt.parseIsoDateTime()
                    )

                    // Crear y retornar el Event
                    val event = Event(
                        id = createEventResponse.idEvent,
                        locationId = createEventResponse.idLocation,
                        date = date.parseIsoDate(),
                        startTime = startTime.parseIsoTime(),
                        endTime = endTime.parseIsoTime(),
                        name = name,
                        description = description,
                        banner = banner,
                        isPublic = isPublic,
                        createdAt = createEventResponse.createdAt.parseIsoDateTime(),
                        updatedAt = createEventResponse.updatedAt.parseIsoDateTime(),
                        location = location
                    )

                    Result.success(event)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
