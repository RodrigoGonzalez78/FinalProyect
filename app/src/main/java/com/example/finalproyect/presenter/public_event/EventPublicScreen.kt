package com.example.finalproyect.presenter.public_event

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.finalproyect.domain.model.TicketType
import com.example.finalproyect.presenter.public_event.components.EventPublicBanner
import com.example.finalproyect.presenter.public_event.components.EventPublicInfo
import com.example.finalproyect.presenter.public_event.components.OrganizerInfo
import com.example.finalproyect.presenter.public_event.components.TicketTypePublicItem

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventPublicScreen(
    navController: NavHostController,
    eventId: String,
    viewModel: EventPublicViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showPurchaseDialog by remember { mutableStateOf(false) }

    // Cargar datos cuando se crea el composable
    LaunchedEffect(eventId) {
        val id = eventId.toIntOrNull()
        if (id != null && id > 0) {
            viewModel.loadEventPublicData(id)
        }
    }

    // Manejar éxito de compra
    LaunchedEffect(uiState.purchaseSuccess) {
        if (uiState.purchaseSuccess) {
            showPurchaseDialog = false
            // Aquí podrías navegar a la pantalla del ticket o mostrar un mensaje
        }
    }

    // Manejar mensajes de éxito
    LaunchedEffect(uiState.successMessage) {
        uiState.successMessage?.let {
            viewModel.clearSuccessMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = uiState.event?.name ?: "Evento",
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
                actions = {
                    IconButton(onClick = { /* Compartir evento */ }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Compartir"
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
                            text = "Cargando evento...",
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
                            text = "Error al cargar el evento",
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
                                    viewModel.loadEventPublicData(id)
                                }
                            }
                        ) {
                            Text("Reintentar")
                        }
                    }
                }
            }

            uiState.hasEventData -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Banner del evento
                    item {
                        EventPublicBanner(
                            event = uiState.event!!,
                            location = uiState.location
                        )
                    }

                    // Información del evento
                    item {
                        EventPublicInfo(
                            event = uiState.event!!,
                            location = uiState.location
                        )
                    }

                    // Información del organizador
                    if (uiState.mainOrganizer != null) {
                        item {
                            OrganizerInfo(
                                organizer = uiState.mainOrganizer!!
                            )
                        }
                    }

                    // Tipos de tickets
                    item {
                        Text(
                            text = "Tickets Disponibles",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (uiState.isLoadingTicketTypes) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    } else if (uiState.hasTicketTypes) {
                        items(uiState.ticketTypes) { ticketType ->
                            TicketTypePublicItem(
                                ticketType = ticketType,
                                onPurchaseClick = {
                                    viewModel.selectTicketType(ticketType)
                                    showPurchaseDialog = true
                                }
                            )
                        }
                    } else {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ConfirmationNumber,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(48.dp)
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    Text(
                                        text = "No hay tickets disponibles",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        textAlign = TextAlign.Center
                                    )

                                    Text(
                                        text = "Los tickets para este evento se han agotado o aún no están disponibles",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }

                    // Espacio adicional al final
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }

    // Diálogo de confirmación de compra
    if (showPurchaseDialog && uiState.selectedTicketType != null) {
        PurchaseConfirmationDialog(
            ticketType = uiState.selectedTicketType!!,
            isPurchasing = uiState.isPurchasing,
            purchaseError = uiState.purchaseError,
            onConfirm = { viewModel.purchaseTicket() },
            onDismiss = {
                showPurchaseDialog = false
                viewModel.clearTicketTypeSelection()
                viewModel.clearPurchaseError()
            }
        )
    }
}

@Composable
private fun PurchaseConfirmationDialog(
    ticketType: TicketType,
    isPurchasing: Boolean,
    purchaseError: String?,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { if (!isPurchasing) onDismiss() },
        title = {
            Text("Confirmar Compra")
        },
        text = {
            Column {
                Text("¿Deseas comprar este ticket?")

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Tipo: ${ticketType.name}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Precio: $${ticketType.price}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )

                if (purchaseError != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = purchaseError,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = !isPurchasing
            ) {
                if (isPurchasing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Comprar")
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isPurchasing
            ) {
                Text("Cancelar")
            }
        }
    )
}
