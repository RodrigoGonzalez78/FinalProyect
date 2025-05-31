package com.example.finalproyect.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LocationDto(
    @SerializedName( "id_location") val idLocation: Long,
    @SerializedName( "name") val name: String,
    @SerializedName( "direction") val direction: String,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("created_at") val createdAt: String, // ISO format
    @SerializedName("updated_at") val updatedAt: String, // ISO format
)