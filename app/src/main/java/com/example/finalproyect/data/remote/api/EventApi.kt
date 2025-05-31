package com.example.finalproyect.data.remote.api

import com.example.finalproyect.data.remote.dto.EventDto
import com.example.finalproyect.data.remote.dto.request.CreateEventRequest
import retrofit2.http.*

interface EventApi {
    @GET("events")
    suspend fun getAllEvents(): List<EventDto>

    @GET("events/{id}")
    suspend fun getEventById(@Path("id") eventId: Long): EventDto

    @POST("events")
    suspend fun createEvent(@Body request: CreateEventRequest): EventDto

    @DELETE("events/{id}")
    suspend fun deleteEvent(@Path("id") eventId: Long)
}