package com.example.finalproyect.presenter.new_event


import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewEventScreen(
    onNavigateBack: NavHostController,
) {
    val context = LocalContext.current

    // Estados para los campos del formulariopackage com.example.eventosapp.data
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedLocation by remember { mutableStateOf<Location?>(null) }
    var selectedDate by remember { mutableStateOf(Calendar.getInstance()) }
    var startTime by remember { mutableStateOf(Calendar.getInstance()) }
    var endTime by remember { mutableStateOf(Calendar.getInstance().apply { add(Calendar.HOUR, 2) }) }
    var isPublic by remember { mutableStateOf(true) }
    var isFree by remember { mutableStateOf(true) }
    var maxGuests by remember { mutableStateOf("100") }
    var bannerUri by remember { mutableStateOf<Uri?>(null) }

    // Estados para diálogos
    var showDatePicker by remember { mutableStateOf(false) }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }
    var showLocationPicker by remember { mutableStateOf(false) }

    // Lanzador para seleccionar imagen
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        bannerUri = uri
    }

    // Validación de formulario
    val isFormValid = name.isNotBlank() &&
            description.isNotBlank() &&
            selectedLocation != null &&
            maxGuests.toIntOrNull() != null

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Nuevo Evento") },
                navigationIcon = {
                    IconButton(onClick = {}) {
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Banner Image Selector
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable { imagePickerLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (bannerUri != null) {
                    AsyncImage(
                        model = bannerUri,
                        contentDescription = "Banner del evento",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Seleccionar imagen de banner",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Nombre del evento
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre del evento") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Descripción
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 5
            )

            // Ubicación
            OutlinedTextField(
                value = selectedLocation?.name ?: "",
                onValueChange = { },
                label = { Text("Ubicación") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showLocationPicker = true }) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Seleccionar ubicación"
                        )
                    }
                }
            )

            // Fecha y hora
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Fecha
                OutlinedTextField(
                    value = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(selectedDate.time),
                    onValueChange = { },
                    label = { Text("Fecha") },
                    modifier = Modifier.weight(1f),
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Seleccionar fecha"
                            )
                        }
                    }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Hora de inicio
                OutlinedTextField(
                    value = SimpleDateFormat("HH:mm", Locale.getDefault()).format(startTime.time),
                    onValueChange = { },
                    label = { Text("Hora inicio") },
                    modifier = Modifier.weight(1f),
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { showStartTimePicker = true }) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Seleccionar hora de inicio"
                            )
                        }
                    }
                )

                // Hora de fin
                OutlinedTextField(
                    value = SimpleDateFormat("HH:mm", Locale.getDefault()).format(endTime.time),
                    onValueChange = { },
                    label = { Text("Hora fin") },
                    modifier = Modifier.weight(1f),
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { showEndTimePicker = true }) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Seleccionar hora de fin"
                            )
                        }
                    }
                )
            }

            // Opciones de evento
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Evento público",
                    style = MaterialTheme.typography.bodyLarge
                )
                Switch(
                    checked = isPublic,
                    onCheckedChange = { isPublic = it }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Evento gratuito",
                    style = MaterialTheme.typography.bodyLarge
                )
                Switch(
                    checked = isFree,
                    onCheckedChange = { isFree = it }
                )
            }

            // Número máximo de invitados
            OutlinedTextField(
                value = maxGuests,
                onValueChange = {
                    if (it.isEmpty() || it.toIntOrNull() != null) {
                        maxGuests = it
                    }
                },
                label = { Text("Máximo de invitados") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            // Botones de acción
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = {},
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancelar")
                }

                Button(
                    onClick = {

                    },
                    modifier = Modifier.weight(1f),
                    enabled = isFormValid
                ) {
                    Text("Guardar")
                }
            }
        }
    }

    // Date Picker Dialog
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate.timeInMillis
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        selectedDate.timeInMillis = it
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // Time Picker Dialogs
    if (showStartTimePicker) {
        val timePickerState = rememberTimePickerState(
            initialHour = startTime.get(Calendar.HOUR_OF_DAY),
            initialMinute = startTime.get(Calendar.MINUTE)
        )

        TimePickerDialog(
            onDismissRequest = { showStartTimePicker = false },
            onConfirm = {
                startTime.set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                startTime.set(Calendar.MINUTE, timePickerState.minute)
                showStartTimePicker = false
            }
        ) {
            TimePicker(state = timePickerState)
        }
    }

    if (showEndTimePicker) {
        val timePickerState = rememberTimePickerState(
            initialHour = endTime.get(Calendar.HOUR_OF_DAY),
            initialMinute = endTime.get(Calendar.MINUTE)
        )

        TimePickerDialog(
            onDismissRequest = { showEndTimePicker = false },
            onConfirm = {
                endTime.set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                endTime.set(Calendar.MINUTE, timePickerState.minute)
                showEndTimePicker = false
            }
        ) {
            TimePicker(state = timePickerState)
        }
    }

    // Location Picker Dialog
    if (showLocationPicker) {
        LocationPickerDialog(
            locations = sampleLocations,
            onDismissRequest = { showLocationPicker = false },
            onLocationSelected = { location ->
                selectedLocation = location
                showLocationPicker = false
            }
        )
    }
}

@Composable
fun TimePickerDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancelar")
            }
        },
        text = { content() }
    )
}

@Composable
fun LocationPickerDialog(
    locations: List<Location>,
    onDismissRequest: () -> Unit,
    onLocationSelected: (Location) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Seleccionar ubicación") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 300.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                locations.forEach { location ->
                    LocationItem(
                        location = location,
                        onClick = { onLocationSelected(location) }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun LocationItem(
    location: Location,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = location.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = location.address,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
