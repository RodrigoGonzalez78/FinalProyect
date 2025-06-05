package com.example.finalproyect.presenter.event_detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.finalproyect.domain.model.Event

@Composable
fun EventOverviewSection(event: Event) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

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
                            value = "0"
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


    }

}