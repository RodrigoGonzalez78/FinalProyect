package com.example.finalproyect.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.finalproyect.data.local.dao.EventDao
import com.example.finalproyect.data.local.dao.LocationDao
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
import com.example.finalproyect.domain.model.PaginatedEvents
import com.example.finalproyect.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val eventApi: EventApi
) : EventRepository {
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
}
