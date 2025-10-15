package com.example.finalproyect.presenter.new_event

import android.Manifest
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproyect.domain.model.Event
import com.example.finalproyect.domain.model.Location
import com.example.finalproyect.domain.repository.GoogleMapsRepository
import com.example.finalproyect.domain.usecase.event.CreateEventUseCase
import com.example.finalproyect.domain.usecase.event.GetEventDetailUseCase
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
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class NewEventViewModel @Inject constructor(
    private val createEventUseCase: CreateEventUseCase,
    private val updateEventUseCase: UpdateEventUseCase,
    private val getEventDetailUseCase: GetEventDetailUseCase,
    private val uploadImageUseCase: UploadImageUseCase,
    private val googleMapsRepository: GoogleMapsRepository,
    private val locationProvider: FusedLocationProviderClient
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewEventUiState())
    val uiState: StateFlow<NewEventUiState> = _uiState.asStateFlow()

    private val _locations = MutableStateFlow<List<Location>>(emptyList())
    val locations: StateFlow<List<Location>> = _locations.asStateFlow()

    private var currentEventId: Int? = null
    private var currentLocationId: Int? = null
    private var isEditMode: Boolean = false

    init {
        loadNearbyLocations()
    }

    fun loadEventForEdit(eventId: String) {
        val id = eventId.toIntOrNull() ?: return
        currentEventId = id
        isEditMode = true

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val result = getEventDetailUseCase(id)

                result.fold(
                    onSuccess = { eventWithPermissions ->
                        val eventDetail = eventWithPermissions.eventDetail

                        // Verificar permisos para editar
                        if (!eventWithPermissions.canManageTicketTypes) {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    errorMessage = "No tienes permisos para editar este evento"
                                )
                            }
                            return@fold
                        }

                        // Parsear fecha y horas por separado
                        // date: "2025-10-29"
                        // startTime: "19:00:29"
                        // endTime: "21:00:29"

                        val eventDate = LocalDate.parse(eventDetail.event.date.toString())
                        val startTime = LocalTime.parse(eventDetail.event.startTime.toString())
                        val endTime = LocalTime.parse(eventDetail.event.endTime.toString())

                        // Combinar fecha con horas para crear LocalDateTime
                        val dateTime = eventDate.atStartOfDay()
                        val startDateTime = LocalDateTime.of(eventDate, startTime)
                        val endDateTime = LocalDateTime.of(eventDate, endTime)

                        // Crear objeto Location desde el evento
                        val location = Location(
                            name = eventDetail.location.name,
                            direction = eventDetail.location.direction,
                            latitude = eventDetail.location.latitude,
                            longitude = eventDetail.location.longitude,
                            id = 0,
                            createdAt = LocalDateTime.now(),
                            updatedAt = LocalDateTime.now()
                        )

                        currentLocationId = eventDetail.location.id.toInt()

                        _uiState.update {
                            it.copy(
                                name = eventDetail.event.name,
                                description = eventDetail.event.description,
                                selectedLocation = location,
                                selectedDate = eventDate,  // Usar eventDate directamente
                                startTime = startTime,     // Usar startTime directamente
                                endTime = endTime,         // Usar endTime directamente
                                isPublic = eventDetail.event.isPublic,
                                bannerUri = null,
                                existingBannerUrl = eventDetail.event.banner,
                                isLoading = false
                            )
                        }
                    },
                    onFailure = { ex ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = ex.message ?: "Error al cargar el evento"
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Error inesperado al cargar el evento"
                    )
                }
            }
        }
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
                                direction = place.address,
                                latitude = place.latitude,
                                longitude = place.longitude,
                                id = 0,
                                createdAt = LocalDateTime.now(),
                                updatedAt = LocalDateTime.now()
                            )
                        }
                    },
                    onFailure = { }
                )
            } catch (e: Exception) { }
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

    fun saveEvent() {
        if (isEditMode) {
            updateEvent()
        } else {
            createEvent()
        }
    }

    private fun createEvent() {
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

    private fun updateEvent() {
        val current = _uiState.value
        val eventId = currentEventId

        if (eventId == null) {
            _uiState.update { it.copy(errorMessage = "ID de evento no válido") }
            return
        }

        if (!current.isFormValidForEdit()) {
            _uiState.update {
                it.copy(errorMessage = "Completa todos los campos correctamente.")
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
                // Si hay una nueva imagen, subirla primero
                if (current.bannerUri != null) {
                    val uploadResult = uploadImageUseCase(current.bannerUri!!)

                    uploadResult.fold(
                        onSuccess = { imageUrl ->
                            updateEventWithImageUrl(eventId, imageUrl)
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
                } else {
                    // Usar la URL existente del banner
                    updateEventWithImageUrl(eventId, current.existingBannerUrl)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Error inesperado al actualizar el evento"
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
                banner = imageUrl,
                isPublic = current.isPublic,
                locationName = loc.name,
                locationDirection = loc.direction,
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

    private suspend fun updateEventWithImageUrl(eventId: Int, imageUrl: String?) {
        val current = _uiState.value
        val loc = current.selectedLocation!!
        val locId = currentLocationId

        if (locId == null) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = "ID de ubicación no válido"
                )
            }
            return
        }

        try {
            // Combinar fecha y hora para crear LocalDateTime
            // El backend espera date, startTime y endTime como LocalDateTime
            val dateTime = LocalDateTime.of(current.selectedDate, LocalTime.MIDNIGHT)
            val startDateTime = LocalDateTime.of(current.selectedDate, current.startTime)
            val endDateTime = LocalDateTime.of(current.selectedDate, current.endTime)

            val result = updateEventUseCase(
                eventId = eventId,
                name = current.name,
                description = current.description,
                date = dateTime,
                startTime = startDateTime,
                endTime = endDateTime,
                banner = imageUrl,
                locationId = locId,
                isPublic = current.isPublic
            )

            result.fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            successMessage = "Evento actualizado correctamente"
                        )
                    }
                },
                onFailure = { ex ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = ex.message ?: "Error al actualizar el evento"
                        )
                    }
                }
            )
        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Error inesperado al actualizar el evento"
                )
            }
        }
    }

    fun isEditMode(): Boolean = isEditMode
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
    val existingBannerUrl: String? = null,

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
                && bannerUri != null
    }

    fun isFormValidForEdit(): Boolean {
        val max = maxGuests.toIntOrNull()
        return name.isNotBlank()
                && description.isNotBlank()
                && selectedLocation != null
                && max != null
                && (bannerUri != null || existingBannerUrl != null)
    }
}