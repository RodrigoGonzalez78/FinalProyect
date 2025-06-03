package com.example.finalproyect.presenter.new_event

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproyect.domain.model.Event
import com.example.finalproyect.domain.usecase.event.CreateEventUseCase
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
    private val createEventUseCase: CreateEventUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewEventUiState())
    val uiState: StateFlow<NewEventUiState> = _uiState.asStateFlow()

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

    fun onIsFreeChange(isFree: Boolean) {
        _uiState.update { it.copy(isFree = isFree, errorMessage = null, successMessage = null) }
    }

    fun onMaxGuestsChange(newMaxGuests: String) {
        _uiState.update {
            it.copy(
                maxGuests = newMaxGuests,
                errorMessage = null,
                successMessage = null
            )
        }
    }

    fun onBannerUriChange(uri: Uri?) {
        _uiState.update { it.copy(bannerUri = uri, errorMessage = null, successMessage = null) }
    }

    fun clearMessages() {
        _uiState.update { it.copy(errorMessage = null, successMessage = null) }
    }


    fun createEvent() {
        val current = _uiState.value

        if (!current.isFormValid()) {
            _uiState.update { it.copy(errorMessage = "Completa todos los campos correctamente.") }
            return
        }

        fun createEvent() {
            val current = _uiState.value

            if (!current.isFormValid()) {
                _uiState.update { it.copy(errorMessage = "Completa todos los campos correctamente.") }
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
                    val bannerStr = current.bannerUri?.toString().orEmpty()
                    val loc = current.selectedLocation!!

                    // Ahora pasamos LocalDate y LocalTime directamente
                    val result: Result<Event> = createEventUseCase(
                        name = current.name,
                        description = current.description,
                        date = current.selectedDate,
                        startTime = current.startTime,
                        endTime = current.endTime,
                        banner = bannerStr,
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
                            errorMessage = e.message ?: "Error inesperado"
                        )
                    }
                }
            }
        }


    }
}


@RequiresApi(Build.VERSION_CODES.O)
data class NewEventUiState constructor(
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
    }
}

data class Location(
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double
)
