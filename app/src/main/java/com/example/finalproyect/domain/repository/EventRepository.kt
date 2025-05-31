package com.example.finalproyect.domain.repository

import com.example.finalproyect.domain.model.Event
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    fun getAllEvents(): Flow<List<Event>>
    fun getEventById(eventId: Long): Flow<Event?>
    suspend fun createEvent(
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
    ): Result<Event>
    suspend fun deleteEvent(eventId: Long): Result<Unit>
    suspend fun refreshEvents(): Result<Unit>
}