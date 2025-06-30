package com.example.finalproyect.data.mappers

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.finalproyect.data.remote.dto.response.ScanTicketResponse
import com.example.finalproyect.domain.model.ScanResult

@RequiresApi(Build.VERSION_CODES.O)
fun ScanTicketResponse.toScanResult(): ScanResult {
    return ScanResult(
        valid = valid,
        message = message,
        ticket = ticket.toTicket(),
        entryCount = entryCount,
        isReentry = isReentry,
        entryTime = entryTime
    )
}