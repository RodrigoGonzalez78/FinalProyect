package com.example.finalproyect.data.repository



import com.example.finalproyect.BuildConfig
import com.example.finalproyect.data.remote.api.GoogleMapsApi
import com.example.finalproyect.domain.model.GooglePlace
import com.example.finalproyect.domain.repository.GoogleMapsRepository
import com.example.finalproyect.utils.Constants
import com.example.finalproyect.utils.Utils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleMapsRepositoryImpl @Inject constructor(
    private val googleMapsApi: GoogleMapsApi
) : GoogleMapsRepository {

    override suspend fun getNearbyPlaces(
        latitude: Double,
        longitude: Double,
        radius: Int,
        type: String
    ): Result<List<GooglePlace>> {
        return try {
            val location = "$latitude,$longitude"
            val response = googleMapsApi.getNearbyPlaces(
                location = location,
                radius = radius,
                type = type,
                key = BuildConfig.MAPS_API_KEY
            )

            if (response.status == "OK") {
                val places = response.results.map { result ->
                    GooglePlace(
                        name = result.name,
                        address = result.vicinity ?: result.formatted_address ?: "",
                        latitude = result.geometry.location.lat,
                        longitude = result.geometry.location.lng,
                        placeId = result.place_id,
                        types = result.types
                    )
                }
                Result.success(places)
            } else {
                Result.failure(Exception("Error al obtener lugares: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
