package com.example.finalproyect.domain.model

data class NearbySearchResponse(
    val results: List<PlaceResult>,
    val status: String,
    val next_page_token: String?
)

data class PlaceResult(
    val name: String,
    val place_id: String,
    val vicinity: String?,
    val formatted_address: String?,
    val geometry: Geometry,
    val types: List<String>
)

data class Geometry(
    val location: Location
) {
    data class Location(
        val lat: Double,
        val lng: Double
    )
}