package com.example.finalproyect.data.remote.api

import com.example.finalproyect.domain.model.NearbySearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleMapsApi {
    @GET("place/nearbysearch/json")
    suspend fun getNearbyPlaces(
        @Query("location") location: String,
        @Query("radius") radius: Int,
        @Query("type") type: String,
        @Query("key") key: String
    ): NearbySearchResponse
}