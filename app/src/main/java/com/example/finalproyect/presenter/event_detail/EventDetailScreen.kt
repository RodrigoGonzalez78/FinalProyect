package com.example.finalproyect.presenter.event_detail

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.finalproyect.presenter.home.Event
import com.example.finalproyect.presenter.home.sampleEvents
import com.example.finalproyect.presenter.navigator.Screen
import java.text.SimpleDateFormat
import java.util.*

data class TicketValidation(
    val id: Long,
    val idEvent: Long,
    val ticketId: String,
    val attendeeName: String,
    val ticketType: String,
    val validationTime: Date,
    val isValid: Boolean
)

// Datos de ejemplo para validaciones de entradas
val sampleTicketValidations = listOf(
    TicketValidation(
        id = 1,
        idEvent = 1,
        ticketId = "TICKET-12345",
        attendeeName = "Ana García",
        ticketType = "VIP",
        validationTime = Date(),
        isValid = true
    ),
    TicketValidation(
        id = 2,
        idEvent = 1,
        ticketId = "TICKET-12346",
        attendeeName = "Pedro Martínez",
        ticketType = "General",
        validationTime = Calendar.getInstance().apply { add(Calendar.MINUTE, -15) }.time,
        isValid = true
    ),
    TicketValidation(
        id = 3,
        idEvent = 1,
        ticketId = "TICKET-12347",
        attendeeName = "Laura Sánchez",
        ticketType = "Premium",
        validationTime = Calendar.getInstance().apply { add(Calendar.MINUTE, -30) }.time,
        isValid = true
    ),
    TicketValidation(
        id = 4,
        idEvent = 1,
        ticketId = "TICKET-12348",
        attendeeName = "Miguel Fernández",
        ticketType = "General",
        validationTime = Calendar.getInstance().apply { add(Calendar.MINUTE, -45) }.time,
        isValid = false
    )
)
data class Notification(
    val id: Long,
    val idEvent: Long,
    val title: String,
    val message: String,
    val createdAt: Date,
    val sentAt: Date?,
    val viewCount: Int
)

// Datos de ejemplo para notificaciones
val sampleNotifications = listOf(
    Notification(
        id = 1,
        idEvent = 1,
        title = "Cambio de horario",
        message = "El evento comenzará una hora más tarde debido a problemas técnicos. Disculpen las molestias.",
        createdAt = Date(),
        sentAt = Date(),
        viewCount = 120
    ),
    Notification(
        id = 2,
        idEvent = 1,
        title = "Información de acceso",
        message = "Recuerden traer su identificación y el código QR de la entrada para agilizar el acceso al evento.",
        createdAt = Calendar.getInstance().apply { add(Calendar.HOUR, -5) }.time,
        sentAt = Calendar.getInstance().apply { add(Calendar.HOUR, -5) }.time,
        viewCount = 98
    ),
    Notification(
        id = 3,
        idEvent = 1,
        title = "Artista invitado",
        message = "Nos complace anunciar que contaremos con un artista sorpresa durante el evento. ¡No se lo pierdan!",
        createdAt = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -1) }.time,
        sentAt = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -1) }.time,
        viewCount = 156
    )
)

data class EventTicketType(
    val id: Long,
    val idEvent: Long,
    val name: String,
    val description: String,
    val price: Double,
    val limit: Int,
    val available: Int
)

// Datos de ejemplo para tipos de entradas
val sampleEventTicketTypes = listOf(
    EventTicketType(
        id = 1,
        idEvent = 1,
        name = "General",
        description = "Acceso general al evento sin asiento reservado",
        price = 15.0,
        limit = 100,
        available = 35
    ),
    EventTicketType(
        id = 2,
        idEvent = 1,
        name = "VIP",
        description = "Acceso preferencial con asiento reservado y una bebida de cortesía",
        price = 35.0,
        limit = 50,
        available = 12
    ),
    EventTicketType(
        id = 3,
        idEvent = 1,
        name = "Premium",
        description = "Acceso completo con asiento en primera fila, barra libre y meet & greet con artistas",
        price = 75.0,
        limit = 20,
        available = 3
    )
)
data class EventStatistics(
    val idEvent: Long,
    val totalTickets: Int,
    val ticketsSold: Int,
    val attendees: Int,
    val revenue: Double,
    val hourlyAttendance: Map<Int, Int>,
    val ticketTypeDistribution: Map<String, Int>
)

// Datos de ejemplo para estadísticas de eventos
val sampleEventStatistics = EventStatistics(
    idEvent = 1,
    totalTickets = 200,
    ticketsSold = 156,
    attendees = 120,
    revenue = 3450.0,
    hourlyAttendance = mapOf(
        18 to 15,
        19 to 45,
        20 to 78,
        21 to 65,
        22 to 35,
        23 to 12
    ),
    ticketTypeDistribution = mapOf(
        "General" to 60,
        "VIP" to 30,
        "Premium" to 10
    )
)

data class EventOrganizer(
    val id: Long,
    val idEvent: Long,
    val idUser: Long,
    val name: String,
    val email: String,
    val role: String
)

// Datos de ejemplo para organizadores de eventos
val sampleEventOrganizers = listOf(
    EventOrganizer(
        id = 1,
        idEvent = 1,
        idUser = 1,
        name = "Carlos Rodríguez",
        email = "carlos.rodriguez@example.com",
        role = "Administrador"
    ),
    EventOrganizer(
        id = 2,
        idEvent = 1,
        idUser = 2,
        name = "María López",
        email = "maria.lopez@example.com",
        role = "Editor"
    ),
    EventOrganizer(
        id = 3,
        idEvent = 1,
        idUser = 3,
        name = "Juan Pérez",
        email = "juan.perez@example.com",
        role = "Asistente"
    )
)




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(navController: NavHostController) {
    var event= sampleEvents.first()
    val context = LocalContext.current

    // Estado para controlar la sección activa
    var activeSection by remember { mutableStateOf("overview") }

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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(event.name, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                navigationIcon = {
                    IconButton(onClick = {navController.navigate(Screen.Home.route)}) {
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Banner del evento
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                AsyncImage(
                    model = event.banner ?: "https://via.placeholder.com/800x400",
                    contentDescription = event.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Gradiente para mejorar legibilidad
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.6f),
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.6f)
                                )
                            )
                        )
                )

                // Información básica del evento
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        text = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale("es", "ES")).format(event.date),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )

                    Text(
                        text = event.locationName ?: "Sin ubicación",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }

                // Indicador de estado del evento
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                ) {
                    Text(
                        text = "Activo",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            // Menú de navegación entre secciones
            ScrollableTabRow(
                selectedTabIndex = when (activeSection) {
                    "overview" -> 0
                    "tickets" -> 1
                    "notifications" -> 2
                    "organizers" -> 3
                    "statistics" -> 4
                    "ticketTypes" -> 5
                    else -> 0
                },
                edgePadding = 16.dp,
                containerColor = MaterialTheme.colorScheme.surface,
                divider = {}
            ) {
                Tab(
                    selected = activeSection == "overview",
                    onClick = { activeSection = "overview" },
                    text = { Text("General") },
                    icon = { Icon(Icons.Outlined.Dashboard, contentDescription = null) }
                )
                Tab(
                    selected = activeSection == "tickets",
                    onClick = { activeSection = "tickets" },
                    text = { Text("Entradas") },
                    icon = { Icon(Icons.Outlined.ConfirmationNumber, contentDescription = null) }
                )
                Tab(
                    selected = activeSection == "notifications",
                    onClick = { activeSection = "notifications" },
                    text = { Text("Avisos") },
                    icon = { Icon(Icons.Outlined.Notifications, contentDescription = null) }
                )
                Tab(
                    selected = activeSection == "organizers",
                    onClick = { activeSection = "organizers" },
                    text = { Text("Equipo") },
                    icon = { Icon(Icons.Outlined.People, contentDescription = null) }
                )
                Tab(
                    selected = activeSection == "statistics",
                    onClick = { activeSection = "statistics" },
                    text = { Text("Estadísticas") },
                    icon = { Icon(Icons.Outlined.BarChart, contentDescription = null) }
                )
                Tab(
                    selected = activeSection == "ticketTypes",
                    onClick = { activeSection = "ticketTypes" },
                    text = { Text("Tipos") },
                    icon = { Icon(Icons.Outlined.Category, contentDescription = null) }
                )
            }

            // Contenido de la sección activa
            Box(modifier = Modifier.fillMaxSize()) {
                when (activeSection) {
                    "overview" -> EventOverviewSection(event)
                    "tickets" -> TicketsSection()
                    "notifications" -> NotificationsSection()
                    "organizers" -> OrganizersSection()
                    "statistics" -> StatisticsSection()
                    "ticketTypes" -> TicketTypesSection()
                }
            }
        }
    }

    // Diálogos
    if (showScanQrDialog) {
        QrScannerDialog(
            onDismiss = { showScanQrDialog = false },
            onQrScanned = { code ->
                // Aquí iría la lógica para validar el código QR
                showScanQrDialog = false
            }
        )
    }

    if (showAddOrganizerDialog) {
        AddOrganizerDialog(
            onDismiss = { showAddOrganizerDialog = false },
            onAddOrganizer = { email, role ->
                // Aquí iría la lógica para añadir un organizador
                showAddOrganizerDialog = false
            }
        )
    }

    if (showAddTicketTypeDialog) {
        AddTicketTypeDialog(
            onDismiss = { showAddTicketTypeDialog = false },
            onAddTicketType = { name, price, description, limit ->
                // Aquí iría la lógica para añadir un tipo de entrada
                showAddTicketTypeDialog = false
            }
        )
    }

    if (showCreateNotificationDialog) {
        CreateNotificationDialog(
            onDismiss = { showCreateNotificationDialog = false },
            onCreateNotification = { title, message, sendNow ->
                // Aquí iría la lógica para crear una notificación
                showCreateNotificationDialog = false
            }
        )
    }
}

@Composable
fun EventOverviewSection(event: Event) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Resumen del evento
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Resumen del evento",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = event.description ?: "Sin descripción",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        InfoItem(
                            icon = Icons.Outlined.People,
                            label = "Invitados",
                            value = "${event.guestsNumber}"
                        )

                        InfoItem(
                            icon = Icons.Outlined.ConfirmationNumber,
                            label = "Entradas vendidas",
                            value = "87%"
                        )

                        InfoItem(
                            icon = Icons.Outlined.CalendarToday,
                            label = "Días restantes",
                            value = "12"
                        )
                    }
                }
            }
        }

        // Acciones rápidas
        item {
            Text(
                text = "Acciones rápidas",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                QuickActionButton(
                    icon = Icons.Outlined.QrCodeScanner,
                    text = "Validar QR",
                    onClick = { /* Navegar a validación */ }
                )

                QuickActionButton(
                    icon = Icons.Outlined.NotificationsActive,
                    text = "Enviar aviso",
                    onClick = { /* Mostrar diálogo de aviso */ }
                )

                QuickActionButton(
                    icon = Icons.Outlined.Edit,
                    text = "Editar evento",
                    onClick = { /* Navegar a edición */ }
                )
            }
        }

        // Actividad reciente
        item {
            Text(
                text = "Actividad reciente",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    ActivityItem(
                        icon = Icons.Outlined.ConfirmationNumber,
                        title = "Nueva entrada vendida",
                        description = "Entrada VIP comprada por Carlos Rodríguez",
                        time = "Hace 5 minutos"
                    )

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    ActivityItem(
                        icon = Icons.Outlined.Person,
                        title = "Nuevo organizador añadido",
                        description = "María López ha sido añadida como organizadora",
                        time = "Hace 2 horas"
                    )

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    ActivityItem(
                        icon = Icons.Outlined.Edit,
                        title = "Evento actualizado",
                        description = "Se ha actualizado la descripción del evento",
                        time = "Hace 1 día"
                    )
                }
            }
        }
    }
}

@Composable
fun TicketsSection() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Resumen de validación
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                TicketStat(
                    value = "156",
                    label = "Validadas",
                    color = MaterialTheme.colorScheme.primary
                )

                TicketStat(
                    value = "43",
                    label = "Pendientes",
                    color = MaterialTheme.colorScheme.tertiary
                )

                TicketStat(
                    value = "78%",
                    label = "Asistencia",
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }

        Text(
            text = "Entradas validadas recientemente",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(sampleTicketValidations) { validation ->
                TicketValidationItem(validation = validation)
            }
        }
    }
}

@Composable
fun NotificationsSection() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Avisos y notificaciones",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(sampleNotifications) { notification ->
                NotificationItem(notification = notification)
            }
        }
    }
}

@Composable
fun OrganizersSection() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Equipo organizador",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(sampleEventOrganizers) { organizer ->
                OrganizerItem(organizer = organizer)
            }
        }
    }
}

@Composable
fun StatisticsSection() {
    val statistics = sampleEventStatistics

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Tarjeta de resumen
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Resumen del evento",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatCard(
                            value = "${statistics.totalTickets}",
                            label = "Entradas",
                            icon = Icons.Outlined.ConfirmationNumber,
                            color = MaterialTheme.colorScheme.primary
                        )

                        StatCard(
                            value = "${statistics.attendees}",
                            label = "Asistentes",
                            icon = Icons.Outlined.People,
                            color = MaterialTheme.colorScheme.secondary
                        )

                        StatCard(
                            value = "$${statistics.revenue}",
                            label = "Ingresos",
                            icon = Icons.Outlined.AttachMoney,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
            }
        }

        // Gráfico de asistencia
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Asistencia por hora",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    // Aquí iría un gráfico real, pero para este ejemplo usamos una representación simple
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(8.dp)
                    ) {
                        // Simulación de barras de gráfico
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            statistics.hourlyAttendance.forEach { (hour, count) ->
                                val height = (count.toFloat() / statistics.hourlyAttendance.values.maxOrNull()!!) * 160

                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Bottom
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .width(20.dp)
                                            .height(height.dp)
                                            .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                            .background(MaterialTheme.colorScheme.primary)
                                    )

                                    Text(
                                        text = "${hour}h",
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Distribución de tipos de entrada
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Distribución de entradas",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    // Aquí iría un gráfico circular real, pero para este ejemplo usamos una representación simple
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        statistics.ticketTypeDistribution.forEach { (type, percentage) ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(CircleShape)
                                        .background(
                                            when (type) {
                                                "General" -> MaterialTheme.colorScheme.primary
                                                "VIP" -> MaterialTheme.colorScheme.secondary
                                                else -> MaterialTheme.colorScheme.tertiary
                                            }
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "$percentage%",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }

                                Text(
                                    text = type,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TicketTypesSection() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Tipos de entrada",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(sampleEventTicketTypes) { ticketType ->
                TicketTypeItem(ticketType = ticketType)
            }
        }
    }
}

@Composable
fun InfoItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )

        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun QuickActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        }

        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun ActivityItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    time: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Text(
            text = time,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun TicketStat(
    value: String,
    label: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = color
        )

        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun TicketValidationItem(validation: TicketValidation) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        if (validation.isValid) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        else MaterialTheme.colorScheme.error.copy(alpha = 0.2f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (validation.isValid) Icons.Default.CheckCircle else Icons.Default.Cancel,
                    contentDescription = null,
                    tint = if (validation.isValid) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = validation.attendeeName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = "Entrada ${validation.ticketType} - ${validation.ticketId}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(validation.validationTime),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(validation.validationTime),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun NotificationItem(notification: Notification) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = notification.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = SimpleDateFormat("dd/MM HH:mm", Locale.getDefault()).format(notification.createdAt),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = notification.message,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Visibility,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "${notification.viewCount} vistas",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row {
                    IconButton(
                        onClick = { /* Editar notificación */ },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = "Editar",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    IconButton(
                        onClick = { /* Eliminar notificación */ },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = "Eliminar",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OrganizerItem(organizer: EventOrganizer) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar del organizador
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = organizer.name.first().toString(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = organizer.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = organizer.email,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(4.dp))

                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = when (organizer.role) {
                        "Administrador" -> MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        "Editor" -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
                        else -> MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f)
                    },
                    contentColor = when (organizer.role) {
                        "Administrador" -> MaterialTheme.colorScheme.primary
                        "Editor" -> MaterialTheme.colorScheme.secondary
                        else -> MaterialTheme.colorScheme.tertiary
                    },
                    modifier = Modifier.wrapContentWidth()
                ) {
                    Text(
                        text = organizer.role,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            IconButton(onClick = { /* Editar permisos */ }) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "Editar permisos",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun StatCard(
    value: String,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color
) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .height(100.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(24.dp)
            )

            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = color,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun TicketTypeItem(ticketType: EventTicketType) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = ticketType.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "$${ticketType.price}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = ticketType.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Disponibles: ${ticketType.available}/${ticketType.limit}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row {
                    IconButton(
                        onClick = { /* Editar tipo de entrada */ },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = "Editar",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    IconButton(
                        onClick = { /* Eliminar tipo de entrada */ },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = "Eliminar",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QrScannerDialog(
    onDismiss: () -> Unit,
    onQrScanned: (String) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Escanear código QR",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Aquí iría el componente real de escaneo de QR
                Box(
                    modifier = Modifier
                        .size(250.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    // Simulación de la cámara
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(8.dp)
                            )
                    )

                    Text(
                        text = "Cámara no disponible en la vista previa",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Coloca el código QR dentro del recuadro para escanearlo",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancelar")
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = {
                            // Simular un escaneo exitoso
                            onQrScanned("TICKET-12345")
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Simular escaneo")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOrganizerDialog(
    onDismiss: () -> Unit,
    onAddOrganizer: (email: String, role: String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("Editor") }
    val roles = listOf("Administrador", "Editor", "Asistente")
    var expanded by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Añadir organizador",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo electrónico") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Email,
                            contentDescription = null
                        )
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedRole,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Rol") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Badge,
                                contentDescription = null
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        roles.forEach { role ->
                            DropdownMenuItem(
                                text = { Text(role) },
                                onClick = {
                                    selectedRole = role
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = { onAddOrganizer(email, selectedRole) },
                        enabled = email.isNotEmpty() && email.contains("@")
                    ) {
                        Text("Añadir")
                    }
                }
            }
        }
    }
}

@Composable
fun AddTicketTypeDialog(
    onDismiss: () -> Unit,
    onAddTicketType: (name: String, price: Double, description: String, limit: Int) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var priceText by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var limitText by remember { mutableStateOf("100") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Nuevo tipo de entrada",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = priceText,
                    onValueChange = {
                        if (it.isEmpty() || it.toDoubleOrNull() != null) {
                            priceText = it
                        }
                    },
                    label = { Text("Precio") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Text(
                            text = "$",
                            modifier = Modifier.padding(start = 12.dp)
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = limitText,
                    onValueChange = {
                        if (it.isEmpty() || it.toIntOrNull() != null) {
                            limitText = it
                        }
                    },
                    label = { Text("Límite de entradas") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            onAddTicketType(
                                name,
                                priceText.toDoubleOrNull() ?: 0.0,
                                description,
                                limitText.toIntOrNull() ?: 100
                            )
                        },
                        enabled = name.isNotEmpty() && priceText.isNotEmpty()
                    ) {
                        Text("Crear")
                    }
                }
            }
        }
    }
}

@Composable
fun CreateNotificationDialog(
    onDismiss: () -> Unit,
    onCreateNotification: (title: String, message: String, sendNow: Boolean) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var sendNow by remember { mutableStateOf(true) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Crear aviso",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    label = { Text("Mensaje") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    minLines = 3
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = sendNow,
                        onCheckedChange = { sendNow = it }
                    )

                    Text(
                        text = "Enviar notificación ahora",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = { onCreateNotification(title, message, sendNow) },
                        enabled = title.isNotEmpty() && message.isNotEmpty()
                    ) {
                        Text("Publicar")
                    }
                }
            }
        }
    }
}
