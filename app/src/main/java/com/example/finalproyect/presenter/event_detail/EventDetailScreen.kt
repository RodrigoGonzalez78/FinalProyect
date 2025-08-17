package com.example.finalproyect.presenter.event_detail

import androidx.compose.foundation.layout.*
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
import com.example.finalproyect.presenter.event_detail.components.EventDetailContent
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.finalproyect.presenter.event_detail.components.AddOrganizerDialog
import com.example.finalproyect.presenter.navigator.navigateToCreateNotification
import com.example.finalproyect.presenter.navigator.navigateToCreateTicketType

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventDetailScreen(
    navController: NavHostController,
    eventId: String,
    viewModel: EventDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val activeSection by viewModel.activeSection.collectAsState()

    var showAddOrganizerDialog by remember { mutableStateOf(false) }

    LaunchedEffect(eventId) {
        viewModel.loadEventDetail(eventId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = uiState.eventDetail?.event?.name ?: "Cargando...",
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
                    // Mostrar rol del usuario en la barra superior
                    if (uiState.isOrganizer) {
                        Surface(
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
                        ) {
                            Text(
                                text = uiState.getRoleName(),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            when {
                activeSection == "tickets" && (uiState.isMainAdmin || uiState.userOrganizerRole == 2) -> {
                    FloatingActionButton(
                        onClick = {

                        },
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCodeScanner,
                            contentDescription = "Escanear QR",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }

                activeSection == "notifications" && uiState.isOrganizer -> {
                    FloatingActionButton(
                        onClick = {
                            navController.navigateToCreateNotification(
                                eventId, uiState.eventDetail?.event?.name
                                    ?: ""
                            )
                        },
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(
                            imageVector = Icons.Default.NotificationsActive,
                            contentDescription = "Crear notificación",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }

                activeSection == "organizers" && uiState.canManageOrganizers -> {
                    FloatingActionButton(
                        onClick = { showAddOrganizerDialog = true },
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(
                            imageVector = Icons.Default.PersonAdd,
                            contentDescription = "Añadir organizador",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }

                activeSection == "ticketTypes" && uiState.canManageTicketTypes -> {
                    FloatingActionButton(
                        onClick = {
                            uiState.eventDetail?.event?.let {
                                navController.navigateToCreateTicketType(
                                    uiState.eventDetail?.event?.id.toString(),
                                    it.name
                                )
                            }
                        },
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Añadir tipo de entrada",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
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
                                viewModel.loadEventDetail(eventId)
                            }
                        ) {
                            Text("Reintentar")
                        }
                    }
                }
            }

            uiState.eventDetail != null -> {
                EventDetailContent(
                    uiState = uiState,
                    activeSection = activeSection,
                    paddingValues = paddingValues,
                    viewModel = viewModel,
                    navController = navController
                )
            }
        }
    }

    if (showAddOrganizerDialog) {
        AddOrganizerDialog(
            onDismiss = { showAddOrganizerDialog = false },
            onAddOrganizer = { email, role ->
                val roleId = when (role) {
                    "Administrador" -> 1
                    "Validador" -> 2
                    else -> 2
                }
                viewModel.createOrganizer(email, roleId)
                showAddOrganizerDialog = false
            }
        )
    }

}