package com.example.finalproyect.data.remote.api


import com.example.finalproyect.data.remote.dto.request.ScanTicketRequest
import com.example.finalproyect.data.remote.dto.response.ScanTicketResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface TicketScanApi {

    @POST("tickets/scan")
    suspend fun scanTicket(
        @Body request: ScanTicketRequest
    ): Response<ScanTicketResponse>
}