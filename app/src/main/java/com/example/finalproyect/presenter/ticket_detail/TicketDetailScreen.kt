package com.example.finalproyect.presenter.ticket_detail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TicketDetailScreen(
    navController: NavHostController,
    eventId: String,
    viewModel: TicketDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Cargar datos cuando se crea el composable
    LaunchedEffect(eventId) {
        val id = eventId.toIntOrNull()
        if (id != null && id > 0) {
            viewModel.loadTicketDetail(id)
        }
    }

    // Manejar mensajes de éxito
    LaunchedEffect(uiState.successMessage) {
        uiState.successMessage?.let {
            // Aquí podrías mostrar un SnackBar si tienes acceso al SnackBarHostState
            viewModel.clearSuccessMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mi Ticket",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Cargando ticket...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            uiState.error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(48.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Error al cargar el ticket",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.error
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = uiState.error ?: "Error desconocido",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                viewModel.clearError()
                                val id = eventId.toIntOrNull()
                                if (id != null) {
                                    viewModel.loadTicketDetail(id)
                                }
                            }
                        ) {
                            Text("Reintentar")
                        }
                    }
                }
            }

            uiState.hasTicketData -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Sección del código QR
                    item {
                        TicketQRSection(
                            ticket = uiState.ticket!!
                        )
                    }

                    // Información del ticket
                    item {
                        TicketInfoSection(
                            ticket = uiState.ticket!!,
                            ticketType = uiState.ticketType
                        )
                    }

                    // Información del evento
                    if (uiState.event != null) {
                        item {
                            EventInfoSection(
                                event = uiState.event!!,
                                location = uiState.location
                            )
                        }
                    }

                    // Acciones del ticket
                    item {
                        TicketActionsSection(
                            onDownload = { viewModel.downloadTicket() },
                            onShare = { viewModel.shareTicket() },
                            isDownloading = uiState.isDownloading,
                            isSharing = uiState.isSharing
                        )
                    }

                    // Espacio adicional al final
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }

            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ConfirmationNumber,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(48.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "No tienes ticket para este evento",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Compra tu ticket para acceder a esta información",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { navController.popBackStack() }
                        ) {
                            Text("Volver al evento")
                        }
                    }
                }
            }
        }
    }
}
