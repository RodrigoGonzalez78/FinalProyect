package com.example.finalproyect.data.remote.api

import com.example.finalproyect.data.remote.dto.request.CreateNotificationRequest
import com.example.finalproyect.data.remote.dto.response.NotificationResponse
import retrofit2.Response
import retrofit2.http.*

interface NotificationApi {

    @POST("events/{id}/notifications")
    suspend fun createNotification(
        @Path("id") eventId: Int,
        @Body request: CreateNotificationRequest
    ): Response<NotificationResponse>

    @GET("events/{id}/notifications")
    suspend fun getNotificationsByEvent(
        @Path("id") eventId: Int
    ): Response<List<NotificationResponse>>

    @DELETE("notifications/{id}")
    suspend fun deleteNotification(
        @Path("id") notificationId: Int
    ): Response<Unit>
}
