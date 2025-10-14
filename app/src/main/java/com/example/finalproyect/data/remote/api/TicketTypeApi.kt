package com.example.finalproyect.data.remote.api


import com.example.finalproyect.data.remote.dto.TicketTypeDto
import com.example.finalproyect.data.remote.dto.request.CreateTicketTypeRequest
import com.example.finalproyect.data.remote.dto.request.UpdateTicketTypeRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TicketTypeApi {

    @POST("events/{id}/ticket-types")
    suspend fun createTicketType(
        @Path("id") eventId: Int,
        @Body request: CreateTicketTypeRequest
    ): Response<TicketTypeDto>

    @GET("events/{id}/ticket-types")
    suspend fun getTicketTypesByEvent(
        @Path("id") eventId: Int
    ): Response<List<TicketTypeDto>>

    @PUT("events/{eventId}/ticket-types/{ticketId}")
    suspend fun updateTicketType(
        @Path("eventId") eventId: Int,
        @Path("ticketId") ticketTypeId: Int,
        @Body request: UpdateTicketTypeRequest
    ): Response<TicketTypeDto>

    @DELETE("events/{eventId}/ticket-types/{ticketId}")
    suspend fun deleteTicketType(
        @Path("eventId") eventId: Int,
        @Path("ticketId") ticketTypeId: Int
    ): Response<Unit>

    @GET("tickets-types/{id}")
    suspend fun getTicketTypeById(
        @Path("id") ticketTypeId: Int
    ): Response<TicketTypeDto>
}
