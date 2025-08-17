package com.example.finalproyect.presenter.new_event

import android.Manifest
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproyect.domain.model.Event
import com.example.finalproyect.domain.repository.GoogleMapsRepository
import com.example.finalproyect.domain.usecase.event.CreateEventUseCase
import com.example.finalproyect.domain.usecase.event.UpdateEventUseCase
import com.example.finalproyect.domain.usecase.upload.UploadImageUseCase
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class NewEventViewModel @Inject constructor(
    private val createEventUseCase: CreateEventUseCase,
    private val googleMapsRepository: GoogleMapsRepository,
    private val locationProvider: FusedLocationProviderClient,
    private val uploadImageUseCase: UploadImageUseCase, // Inyectar el repositorio de Google Maps
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewEventUiState())
    val uiState: StateFlow<NewEventUiState> = _uiState.asStateFlow()


    private val _locations = MutableStateFlow<List<Location>>(emptyList())
    val locations: StateFlow<List<Location>> = _locations.asStateFlow()

    init {
        loadNearbyLocations()
    }

    private fun loadNearbyLocations() {
        viewModelScope.launch {
            try {

                val latitude = -27.467
                val longitude = -58.830
                val radius = 5000 // 5km

                val result = googleMapsRepository.getNearbyPlaces(
                    latitude = latitude,
                    longitude = longitude,
                    radius = radius,
                    type = "establishment"
                )

                result.fold(
                    onSuccess = { places ->
                        _locations.value = places.map { place ->
                            Location(
                                name = place.name,
                                address = place.address,
                                latitude = place.latitude,
                                longitude = place.longitude
                            )
                        }
                    },
                    onFailure = {
                    }
                )
            } catch (e: Exception) {
            }
        }
    }

    fun onNameChange(newName: String) {
        _uiState.update { it.copy(name = newName, errorMessage = null, successMessage = null) }
    }

    fun onDescriptionChange(newDescription: String) {
        _uiState.update {
            it.copy(
                description = newDescription,
                errorMessage = null,
                successMessage = null
            )
        }
    }

    fun onLocationSelected(location: Location) {
        _uiState.update {
            it.copy(
                selectedLocation = location,
                errorMessage = null,
                successMessage = null
            )
        }
    }

    fun onDateChange(newDate: LocalDate) {
        _uiState.update {
            it.copy(
                selectedDate = newDate,
                errorMessage = null,
                successMessage = null
            )
        }
    }

    fun onStartTimeChange(newStart: LocalTime) {
        _uiState.update {
            it.copy(
                startTime = newStart,
                errorMessage = null,
                successMessage = null
            )
        }
    }

    fun onEndTimeChange(newEnd: LocalTime) {
        _uiState.update { it.copy(endTime = newEnd, errorMessage = null, successMessage = null) }
    }

    fun onIsPublicChange(isPublic: Boolean) {
        _uiState.update { it.copy(isPublic = isPublic, errorMessage = null, successMessage = null) }
    }

    fun onBannerUriChange(uri: Uri?) {
        _uiState.update { it.copy(bannerUri = uri, errorMessage = null, successMessage = null) }
    }

    fun clearMessages() {
        _uiState.update { it.copy(errorMessage = null, successMessage = null) }
    }

    fun createEvent() {
        val current = _uiState.value

        if (!current.isFormValid() || current.bannerUri == null) {
            _uiState.update {
                it.copy(errorMessage = "Completa todos los campos correctamente y selecciona una imagen.")
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null,
                    successMessage = null
                )
            }

            try {
                // Primero subir la imagen
                val uploadResult = uploadImageUseCase(current.bannerUri!!)

                uploadResult.fold(
                    onSuccess = { imageUrl ->

                        createEventWithImageUrl(imageUrl)
                    },
                    onFailure = { ex ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = ex.message ?: "Error al subir la imagen"
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Error inesperado al subir la imagen"
                    )
                }
            }
        }
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    fun fetchLastKnownLocation(onResult: (LatLng) -> Unit) {
        locationProvider.lastLocation.addOnSuccessListener { loc ->
            loc?.let {
                onResult(LatLng(it.latitude, it.longitude))
            }
        }
    }

    private suspend fun createEventWithImageUrl(imageUrl: String) {
        val current = _uiState.value
        val loc = current.selectedLocation!!

        try {
            val result: Result<Event> = createEventUseCase(
                name = current.name,
                description = current.description,
                date = current.selectedDate,
                startTime = current.startTime,
                endTime = current.endTime,
                banner = imageUrl, // URL de la imagen subida
                isPublic = current.isPublic,
                locationName = loc.name,
                locationDirection = loc.address,
                locationLatitude = loc.latitude,
                locationLongitude = loc.longitude
            )

            result.fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            successMessage = "Evento creado correctamente"
                        )
                    }
                },
                onFailure = { ex ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = ex.message ?: "Error al crear el evento"
                        )
                    }
                }
            )
        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Error inesperado al crear el evento"
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
data class NewEventUiState(
    val name: String = "",
    val description: String = "",
    val selectedLocation: Location? = null,
    val selectedDate: LocalDate = LocalDate.now(),
    val startTime: LocalTime = LocalTime.now(),
    val endTime: LocalTime = LocalTime.now().plusHours(2),
    val isPublic: Boolean = true,
    val isFree: Boolean = true,
    val maxGuests: String = "100",
    val bannerUri: Uri? = null,

    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
) {
    fun isFormValid(): Boolean {
        val max = maxGuests.toIntOrNull()
        return name.isNotBlank()
                && description.isNotBlank()
                && selectedLocation != null
                && max != null
                && startTime < endTime
                && bannerUri != null // Validar que hay imagen seleccionada
    }
}

data class Location(
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double
)

