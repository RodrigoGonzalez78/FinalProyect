package com.example.finalproyect.domain.model

import java.time.LocalTime

data class Notification(
    val title:String,
    val message:String,
    val createdAt: LocalTime
)