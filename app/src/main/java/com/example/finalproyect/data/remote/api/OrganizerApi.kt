package com.example.finalproyect.data.remote.api

import com.example.finalproyect.data.remote.dto.OrganizerDto
import com.example.finalproyect.data.remote.dto.request.CreateOrganizerRequest
import com.example.finalproyect.data.remote.dto.request.UpdateOrganizerRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface OrganizerApi {

    @POST("events/{id}/organizers")
    suspend fun createOrganizer(
        @Path("id") eventId: Int,
        @Body request: CreateOrganizerRequest
    ): Response<OrganizerDto>

    @GET("events/{id}/organizers")
    suspend fun getOrganizersByEvent(
        @Path("id") eventId: Int
    ): Response<List<OrganizerDto>>

    @PUT("events/{id}/organizers/{organizerId}")
    suspend fun updateOrganizer(
        @Path("id") eventId: Int,
        @Path("organizerId") organizerId: Int,
        @Body request: UpdateOrganizerRequest
    ): Response<OrganizerDto>

    @DELETE("events/{id}/organizers/{organizerId}")
    suspend fun deleteOrganizer(
        @Path("id") eventId: Int,
        @Path("organizerId") organizerId: Int
    ): Response<Unit>
}
