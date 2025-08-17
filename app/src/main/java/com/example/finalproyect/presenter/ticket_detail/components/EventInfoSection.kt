package com.example.finalproyect.presenter.ticket_detail.components


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.finalproyect.domain.model.Event
import com.example.finalproyect.domain.model.Location
import com.example.finalproyect.utils.toLocalDateTime


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventInfoSection(
    event: Event,
    location: Location?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Título de la sección
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Event,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Información del Evento",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Nombre del evento
            Text(
                text = event.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Información del evento
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Fecha y hora
                EventInfoRow(
                    icon = Icons.Outlined.Schedule,
                    label = "Fecha y hora",
                    value = "${event.startTime.toString().toLocalDateTime()} - ${event.endTime.toString().toLocalDateTime()}"
                )

                // Ubicación
                if (location != null) {
                    EventInfoRow(
                        icon = Icons.Outlined.LocationOn,
                        label = "Ubicación",
                        value = "${location.name}"
                    )
                }

                // Descripción
                if (event.description.isNotBlank()) {
                    EventInfoRow(
                        icon = Icons.Outlined.Description,
                        label = "Descripción",
                        value = event.description
                    )
                }

                // Estado del evento
                EventInfoRow(
                    icon = Icons.Outlined.Info,
                    label = "Estado",
                    value =  "else",// if (event.isActive) "Activo" else "Inactivo",
                  valueColor = //if (event.isActive)
                        MaterialTheme.colorScheme.primary
//                    else
//                        MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

@Composable
private fun EventInfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    valueColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = valueColor
            )
        }
    }
}
