package com.example.finalproyect.domain.model

data class ScanResult(
    val valid: Boolean,
    val message: String,
    val ticket: Ticket,
    val entryCount: Int,
    val isReentry: Boolean,
    val entryTime: String
)