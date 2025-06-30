package com.example.finalproyect.presenter.scanner

import com.example.finalproyect.domain.model.ScanResult
import com.example.finalproyect.domain.usecase.ticket.ScanTicketUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScannerViewModel @Inject constructor(
    private val scanTicketUseCase: ScanTicketUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScannerUiState())
    val uiState: StateFlow<ScannerUiState> = _uiState.asStateFlow()

    fun setEventInfo(eventId: Int, eventName: String) {
        _uiState.value = _uiState.value.copy(
            eventId = eventId,
            eventName = eventName
        )
    }

    fun scanQRCode(qrCode: String) {
        val eventId = _uiState.value.eventId ?: return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                isScanning = false
            )

            scanTicketUseCase(qrCode, eventId)
                .onSuccess { scanResult ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        scanResult = scanResult,
                        showResult = true
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Error desconocido",
                        showResult = true
                    )
                }
        }
    }

    fun resetScanner() {
        _uiState.value = _uiState.value.copy(
            isScanning = true,
            scanResult = null,
            errorMessage = null,
            showResult = false,
            isLoading = false
        )
    }

    fun dismissResult() {
        _uiState.value = _uiState.value.copy(
            showResult = false,
            scanResult = null,
            errorMessage = null
        )
    }
}

data class ScannerUiState(
    val isLoading: Boolean = false,
    val scanResult: ScanResult? = null,
    val errorMessage: String? = null,
    val isScanning: Boolean = true,
    val eventId: Int? = null,
    val eventName: String? = null,
    val showResult: Boolean = false
)

