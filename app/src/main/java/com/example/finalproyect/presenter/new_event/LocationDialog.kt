package com.example.finalproyect.presenter.new_event
import android.Manifest
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.maps.android.compose.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapLocationPickerDialog(
    initialPosition: LatLng = LatLng(-27.467, -58.830),
    onDismissRequest: () -> Unit,
    onLocationPicked: (Location) -> Unit
) {
    // 1. Estado del permiso
    val locationPermissionState = rememberPermissionState(
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    // 2. Estado de la cámara
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialPosition, 13f)
    }

    // 3. Estado del marcador (inicial en initialPosition)
    val markerState = rememberMarkerState(position = initialPosition)

    // 4. Estados para geocodificación
    val context = LocalContext.current
    val geocoder = remember(context) {
        Geocoder(context, Locale.getDefault())
    }
    var locationName by remember { mutableStateOf("Cargando...") }
    var isLoadingLocation by remember { mutableStateOf(false) }

    // 5. Función para obtener el nombre de la ubicación
    suspend fun getLocationName(latLng: LatLng): String {
        return withContext(Dispatchers.IO) {
            try {
                val addresses = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    // Para Android 13+
                    suspendCoroutine<List<Address>> { continuation ->
                        geocoder.getFromLocation(
                            latLng.latitude,
                            latLng.longitude,
                            1
                        ) { addresses ->
                            continuation.resume(addresses)
                        }
                    }
                } else {
                    // Para versiones anteriores
                    @Suppress("DEPRECATION")
                    geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1) ?: emptyList()
                }

                if (addresses.isNotEmpty()) {
                    val address = addresses[0]
                    // Construir nombre de ubicación con la información disponible
                    buildString {
                        address.featureName?.let { append(it) }
                        address.thoroughfare?.let {
                            if (isNotEmpty()) append(", ")
                            append(it)
                        }
                        address.locality?.let {
                            if (isNotEmpty()) append(", ")
                            append(it)
                        }
                        address.adminArea?.let {
                            if (isNotEmpty()) append(", ")
                            append(it)
                        }
                        address.countryName?.let {
                            if (isNotEmpty()) append(", ")
                            append(it)
                        }
                    }.takeIf { it.isNotEmpty() }
                        ?: "Ubicación desconocida"
                } else {
                    "Ubicación no encontrada"
                }
            } catch (e: Exception) {
                "Error al obtener ubicación"
            }
        }
    }

    // 6. Efecto para obtener el nombre de la ubicación inicial
    LaunchedEffect(markerState.position) {
        isLoadingLocation = true
        locationName = getLocationName(markerState.position)
        isLoadingLocation = false
    }

    // 7. Solicita permiso al abrir el diálogo
    LaunchedEffect(Unit) {
        if (!locationPermissionState.status.isGranted) {
            locationPermissionState.launchPermissionRequest()
        }
    }

    // Verificar si tiene permisos
    val hasLocationPermission = locationPermissionState.status.isGranted

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Seleccionar en el mapa") },
        text = {
            Column {
                // Mostrar el nombre de la ubicación actual
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        if (isLoadingLocation) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Obteniendo ubicación...",
                                style = MaterialTheme.typography.bodySmall
                            )
                        } else {
                            Text(
                                text = locationName,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                // Mapa
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    GoogleMap(
                        modifier = Modifier.matchParentSize(),
                        cameraPositionState = cameraPositionState,
                        properties = MapProperties(
                            isMyLocationEnabled = hasLocationPermission
                        ),
                        uiSettings = MapUiSettings(
                            myLocationButtonEnabled = hasLocationPermission
                        ),
                        onMapClick = { latLng ->
                            // Al hacer tap, movemos el marcador
                            markerState.position = latLng
                        }
                    ) {
                        Marker(
                            state = markerState,
                            title = if (isLoadingLocation) "Cargando..." else locationName
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    // Al confirmar, devolvemos la posición del marcador con el nombre real
                    val lat = markerState.position.latitude
                    val lng = markerState.position.longitude
                    onLocationPicked(
                        Location(
                            name = locationName,
                            address = if (locationName.contains("Error") || locationName.contains("desconocida")) {
                                "(${lat.format(5)}, ${lng.format(5)})"
                            } else {
                                locationName
                            },
                            latitude = lat,
                            longitude = lng
                        )
                    )
                    onDismissRequest()
                },
                enabled = !isLoadingLocation
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancelar")
            }
        }
    )
}

// Extensión para formatear números
fun Double.format(digits: Int) = "%.${digits}f".format(this)