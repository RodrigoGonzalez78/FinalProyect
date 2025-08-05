package com.example.finalproyect.presenter.event_detail

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.finalproyect.presenter.event_detail.components.ticket_type_section.AddTicketTypeDialog
import com.example.finalproyect.presenter.event_detail.components.notification_section.CreateNotificationDialog
import com.example.finalproyect.presenter.event_detail.components.EventDetailContent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.finalproyect.presenter.event_detail.components.AddOrganizerDialog
import com.example.finalproyect.presenter.event_detail.components.QrScannerDialog


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventDetailScreen(
    navController: NavHostController,
    eventId: String,
    viewModel: EventDetailsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val activeSection by viewModel.activeSection.collectAsState()

    // Estados para diálogos
    var showScanQrDialog by remember { mutableStateOf(false) }
    var showAddOrganizerDialog by remember { mutableStateOf(false) }
    var showAddTicketTypeDialog by remember { mutableStateOf(false) }
    var showCreateNotificationDialog by remember { mutableStateOf(false) }

    // Estado para permisos de cámara
    val cameraPermissionState = remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        cameraPermissionState.value = isGranted
        if (isGranted) {
            showScanQrDialog = true
        }
    }

    // Cargar detalles del evento cuando se crea el composable
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
                            shape = RoundedCornerShape(16.dp),
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
            // FAB contextual según la sección activa y permisos del usuario
            when {
                activeSection == "tickets" && (uiState.isMainAdmin || uiState.userOrganizerRole == 2) -> {
                    FloatingActionButton(
                        onClick = {
                            if (cameraPermissionState.value) {
                                showScanQrDialog = true
                            } else {
                                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                            }
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
                        onClick = { showCreateNotificationDialog = true },
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
                        onClick = { showAddTicketTypeDialog = true },
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
                    onSectionChange = { section -> viewModel.setActiveSection(section) },
                    onPurchaseTicket = { ticketTypeId -> viewModel.purchaseTicket(ticketTypeId) },
                    onDeleteEvent = { viewModel.deleteEvent() },
                    onValidateTicket = { qrCode ->
                        // Implementar validación del ticket
                    },
                    onCreateTicketType = { name, price, description, available ->
                        viewModel.createTicketType(name, price, description, available)
                    },
                    onUpdateTicketType = { ticketTypeId, name, price, description, available ->
                        viewModel.updateTicketType(
                            ticketTypeId,
                            name,
                            price,
                            description,
                            available
                        )
                    },
                    onCreateOrganizer = { email, roleId ->
                        viewModel.createOrganizer(email, roleId)
                    },
                    onUpdateOrganizerRole = { organizerId, newRoleId ->
                        viewModel.updateOrganizerRole(organizerId, newRoleId)
                    },
                    onDeleteOrganizer = { organizerId ->
                        viewModel.deleteOrganizer(organizerId)
                    },
                    onDeleteTicketType = { ticketTypeId ->
                        viewModel.deleteTicketType(ticketTypeId)
                    },
                    onNavigateToTicketDetail = {
                        // Navegar a la pantalla de detalles del ticket
                        navController.navigate("ticket_detail/$eventId")
                    },
                    navController = navController
                )
            }
        }
    }

    // Diálogos modales
    if (showAddOrganizerDialog) {
        AddOrganizerDialog(
            onDismiss = { showAddOrganizerDialog = false },
            onAddOrganizer = { email, role ->
                val roleId = when (role) {
                    "Administrador" -> 2
                    "Editor" -> 3
                    "Asistente" -> 4
                    else -> 4
                }
                viewModel.createOrganizer(email, roleId)
                showAddOrganizerDialog = false
            }
        )
    }

    if (showScanQrDialog) {
        QrScannerDialog(
            onDismiss = { showScanQrDialog = false },
            onQrScanned = { code ->
                showScanQrDialog = false
            }
        )
    }

    if (showAddTicketTypeDialog) {
        AddTicketTypeDialog(
            onDismiss = { showAddTicketTypeDialog = false },
            onAddTicketType = { name, price, description, limit ->
                viewModel.createTicketType(name, price, description, limit)
                showAddTicketTypeDialog = false
            }
        )
    }

    if (showCreateNotificationDialog) {
        CreateNotificationDialog(
            onDismiss = { showCreateNotificationDialog = false },
            onCreateNotification = { title, message, sendNow ->
                showCreateNotificationDialog = false
            }
        )
    }
}
