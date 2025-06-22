package com.example.finalproyect.data.remote.api

import com.example.finalproyect.data.remote.dto.TicketDto
import com.example.finalproyect.data.remote.dto.response.PurchaseTicketResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TicketApi {

    @POST("tickets/purchase")
    suspend fun purchaseTicket(
        @Query("ticketTypeID") ticketTypeId: Int
    ): Response<PurchaseTicketResponse>

    @GET("events/{id}/tickets")
    suspend fun getUserTicketForEvent(
        @Path("id") eventId: Int
    ): Response<TicketDto>
}
