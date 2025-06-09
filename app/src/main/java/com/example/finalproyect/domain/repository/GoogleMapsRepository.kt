package com.example.finalproyect.domain.repository

import com.example.finalproyect.domain.model.GooglePlace

interface GoogleMapsRepository {
    suspend fun getNearbyPlaces(
        latitude: Double,
        longitude: Double,
        radius: Int,
        type: String
    ): Result<List<GooglePlace>>
}