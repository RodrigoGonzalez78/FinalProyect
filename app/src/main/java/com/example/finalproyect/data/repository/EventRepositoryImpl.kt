package com.example.finalproyect.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.finalproyect.data.local.dao.EventDao
import com.example.finalproyect.data.local.dao.LocationDao
import com.example.finalproyect.data.mappers.toEvent
import com.example.finalproyect.data.mappers.toEventEntity
import com.example.finalproyect.data.mappers.toLocationEntity
import com.example.finalproyect.data.remote.api.EventApi
import com.example.finalproyect.data.remote.dto.request.CreateEventRequest
import com.example.finalproyect.data.remote.dto.request.EventRequest
import com.example.finalproyect.data.remote.dto.request.LocationRequest
import com.example.finalproyect.domain.model.Event
import com.example.finalproyect.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val eventApi: EventApi,
    private val eventDao: EventDao,
    private val locationDao: LocationDao
) : EventRepository {

    override fun getAllEvents(): Flow<List<Event>> {
        return eventDao.getAllEvents().map { eventWithLocations ->
            eventWithLocations.map { it.toEvent() }
        }
    }

    override fun getEventById(eventId: Long): Flow<Event?> {
        return eventDao.getEventById(eventId).map { it?.toEvent() }
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

            // Guardar en la base de datos local
            val locationEntity = response.toLocationEntity()
            locationDao.insertLocation(locationEntity)

            val eventEntity = response.toEventEntity()
            eventDao.insertEvent(eventEntity)

            // Obtener el evento con su ubicación
            val eventWithLocation = eventDao.getEventById(response.idEvent).map { it?.toEvent() }
            Result.success(eventWithLocation.firstOrNull() ?: throw Exception("Error al obtener el evento creado"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteEvent(eventId: Long): Result<Unit> {
        return try {
            eventApi.deleteEvent(eventId)
            // También eliminar de la base de datos local
            val event = eventDao.getEventById(eventId).firstOrNull()?.event
            event?.let { eventDao.deleteEvent(it) }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun refreshEvents(): Result<Unit> {
        return try {
            val remoteEvents = eventApi.getAllEvents()

            // Guardar ubicaciones
            remoteEvents.forEach { eventDto ->
                val locationEntity = eventDto.toLocationEntity()
                locationDao.insertLocation(locationEntity)

                val eventEntity = eventDto.toEventEntity()
                eventDao.insertEvent(eventEntity)
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

