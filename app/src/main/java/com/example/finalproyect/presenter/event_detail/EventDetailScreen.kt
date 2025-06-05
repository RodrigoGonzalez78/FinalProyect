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
import com.example.finalproyect.presenter.event_detail.components.organizer_section.AddOrganizerDialog
import com.example.finalproyect.presenter.event_detail.components.ticket_type_section.AddTicketTypeDialog
import com.example.finalproyect.presenter.event_detail.components.notification_section.CreateNotificationDialog
import com.example.finalproyect.presenter.event_detail.components.EventDetailContent
import com.example.finalproyect.presenter.event_detail.components.dialogs.QrScannerDialog
import com.example.finalproyect.presenter.navigator.Screen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    navController: NavHostController,
    eventId: String,
    viewModel: EventDetailsViewModels = hiltViewModel()
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
                    IconButton(onClick = { navController.navigate(Screen.Home.route) }) {
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
        },
        floatingActionButton = {
            when (activeSection) {
                "tickets" -> {
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

                "notifications" -> {
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

                "organizers" -> {
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

                "ticketTypes" -> {
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
                    CircularProgressIndicator()
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
                        verticalArrangement = Arrangement.Center
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

                        Text(
                            text = uiState.error?:"",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { viewModel.loadEventDetail(eventId) }
                        ) {
                            Text("Reintentar")
                        }
                    }
                }
            }

            uiState.eventDetail != null -> {
                EventDetailContent(
                    eventDetail = uiState.eventDetail!!,
                    activeSection = activeSection,
                    paddingValues = paddingValues,
                    onSectionChange = { section -> viewModel.setActiveSection(section) }
                )
            }
        }
    }

    // Diálogos
    if (showScanQrDialog) {
        QrScannerDialog(
            onDismiss = { showScanQrDialog = false },
            onQrScanned = { code ->
                viewModel.validateTicket(code)
                showScanQrDialog = false
            }
        )
    }

    if (showAddOrganizerDialog) {
        AddOrganizerDialog(
            onDismiss = { showAddOrganizerDialog = false },
            onAddOrganizer = { email, role ->
                viewModel.addOrganizer(email, role)
                showAddOrganizerDialog = false
            }
        )
    }

    if (showAddTicketTypeDialog) {
        AddTicketTypeDialog(
            onDismiss = { showAddTicketTypeDialog = false },
            onAddTicketType = { name, price, description, limit ->
                viewModel.addTicketType(name, price, description, limit)
                showAddTicketTypeDialog = false
            }
        )
    }

    if (showCreateNotificationDialog) {
        CreateNotificationDialog(
            onDismiss = { showCreateNotificationDialog = false },
            onCreateNotification = { title, message, sendNow ->
                viewModel.createNotification(title, message, sendNow)
                showCreateNotificationDialog = false
            }
        )
    }
}
