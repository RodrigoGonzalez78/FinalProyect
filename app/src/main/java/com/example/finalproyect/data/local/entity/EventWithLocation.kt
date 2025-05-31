package com.example.finalproyect.data.local.entity


import androidx.room.Embedded
import androidx.room.Relation

data class EventWithLocation(
    @Embedded val event: EventEntity,
    @Relation(
        parentColumn = "locationId",
        entityColumn = "id"
    )
    val location: LocationEntity
)
