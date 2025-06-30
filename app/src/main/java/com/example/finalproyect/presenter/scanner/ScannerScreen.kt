package com.example.finalproyect.presenter.scanner

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.finalproyect.presenter.scanner.components.QRScannerView
import com.example.finalproyect.presenter.scanner.components.ScanResultDialog
import com.example.finalproyect.presenter.scanner.components.ScannerOverlay
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ScannerScreen(
    eventId: Int,
    eventName: String,
    onNavigateBack: () -> Unit,
    viewModel: ScannerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var flashEnabled by remember { mutableStateOf(false) }

    // Estado de permiso de cámara
    val cameraPermState = rememberPermissionState(Manifest.permission.CAMERA)

    // Pedimos permiso la primera vez que entra la pantalla
    LaunchedEffect(Unit) {
        cameraPermState.launchPermissionRequest()
    }

    // Inicializamos datos del evento
    LaunchedEffect(eventId, eventName) {
        viewModel.setEventInfo(eventId, eventName)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Validar Entrada",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = eventName,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    if (cameraPermState.status.isGranted) {
                        IconButton(onClick = { flashEnabled = !flashEnabled }) {
                            Icon(
                                imageVector = if (flashEnabled) Icons.Default.FlashOn else Icons.Default.FlashOff,
                                contentDescription = if (flashEnabled) "Desactivar flash" else "Activar flash"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                // Caso: permiso no concedido
                !cameraPermState.status.isGranted -> {
                    CameraPermissionContent(
                        shouldShowRationale = cameraPermState.status.shouldShowRationale,
                        onRequestPermission = { cameraPermState.launchPermissionRequest() }
                    )
                }

                // Caso: permiso concedido -> mostramos cámara
                else -> {
                    QRScannerView(
                        onQRCodeScanned = { qrCode ->
                            if (uiState.isScanning) {
                                viewModel.scanQRCode(qrCode)
                            }
                        },
                        isScanning = uiState.isScanning,
                        modifier = Modifier.fillMaxSize()
                    )

                    ScannerOverlay()

                    if (uiState.isScanning && !uiState.isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .padding(32.dp)
                        ) {
                            Text(
                                text = "Apunta la cámara hacia el código QR del ticket",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    if (uiState.isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            // Diálogo con el resultado del escaneo
            if (uiState.showResult) {
                ScanResultDialog(
                    scanResult = uiState.scanResult,
                    errorMessage = uiState.errorMessage,
                    onDismiss = {
                        viewModel.dismissResult()
                        viewModel.resetScanner()
                    },
                    onScanAgain = {
                        viewModel.dismissResult()
                        viewModel.resetScanner()
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun CameraPermissionContent(
    shouldShowRationale: Boolean,
    onRequestPermission: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                text = if (shouldShowRationale) {
                    "La cámara es necesaria para escanear códigos QR. Por favor, concede el permiso."
                } else {
                    "La app requiere acceso a la cámara para escanear códigos QR."
                },
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = onRequestPermission) {
                Text("Conceder permiso")
            }
        }
    }
}