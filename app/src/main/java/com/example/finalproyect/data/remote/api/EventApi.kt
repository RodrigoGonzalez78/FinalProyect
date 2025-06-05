package com.example.finalproyect.data.remote.api

import com.example.finalproyect.data.remote.dto.EventDto
import com.example.finalproyect.data.remote.dto.request.CreateEventRequest
import com.example.finalproyect.data.remote.dto.request.UpdateEventRequest
import com.example.finalproyect.data.remote.dto.response.EventDetailResponse
import com.example.finalproyect.data.remote.dto.response.PaginatedEventsResponse
import retrofit2.Response
import retrofit2.http.*

interface EventApi {

    @GET("events/public")
    suspend fun searchPublicEvents(
        @Query("name") name: String,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 10
    ): Response<PaginatedEventsResponse>

    @GET("users/events")
    suspend fun getUserEvents(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 10
    ): Response<PaginatedEventsResponse>

    @GET("events/{id}")
    suspend fun getEventById(
        @Path("id") eventId: Int
    ): Response<EventDetailResponse>

    @PUT("events/{id}")
    suspend fun updateEvent(
        @Path("id") eventId: Int,
        @Body request: UpdateEventRequest
    ): Response<EventDto>

    @DELETE("events/{id}")
    suspend fun deleteEvent(
        @Path("id") eventId: Int
    ): Response<Unit>

    @POST("events")
    suspend fun createEvent(@Body request: CreateEventRequest): Response<EventDto>
}