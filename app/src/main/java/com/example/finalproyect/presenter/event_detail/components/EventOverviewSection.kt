package com.example.finalproyect.presenter.event_detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.QrCode
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.finalproyect.presenter.event_detail.EventDetailUiState
import com.example.finalproyect.presenter.navigator.AppDestination
import com.example.finalproyect.presenter.navigator.navigateToCreateNotification
import com.example.finalproyect.presenter.navigator.navigateToEditEvent


@Composable
fun EventOverviewSection(
    navController: NavHostController,
    uiState: EventDetailUiState,
) {
    val eventDetail = uiState.eventDetail ?: return

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
                        text = eventDetail.event.description ?: "Sin descripción",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        InfoItem(
                            icon = Icons.Outlined.People,
                            label = "Organizadores",
                            value = uiState.organizers.size.toString()
                        )

                        InfoItem(
                            icon = Icons.Outlined.ConfirmationNumber,
                            label = "Tipos de entrada",
                            value = uiState.ticketTypes.size.toString()
                        )

                        InfoItem(
                            icon = Icons.Outlined.CalendarToday,
                            label = "Estado",
                            value = if (uiState.eventDetail?.event?.isPublic == true) "Público" else "Privado"
                        )
                    }
                }
            }
        }

        // Acciones rápidas según el rol del usuario
        item {
            Text(
                text = "Acciones disponibles",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            when {
                // Admin principal - todas las acciones
                uiState.isMainAdmin -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        QuickActionButton(
                            icon = Icons.Outlined.QrCodeScanner,
                            text = "Validar QR",
                            onClick = {
                                navController.navigate(
                                    AppDestination.Scanner(
                                        uiState.eventDetail!!.event.id.toInt(),
                                        uiState.eventDetail!!.event.name
                                    )
                                )
                            }
                        )

                        QuickActionButton(
                            icon = Icons.Outlined.NotificationsActive,
                            text = "Enviar aviso",
                            onClick = {
                                navController.navigateToCreateNotification(
                                    uiState.eventDetail!!.event.id.toString(),
                                    uiState.eventDetail!!.event.name
                                )
                            }
                        )

                        QuickActionButton(
                            icon = Icons.Outlined.Edit,
                            text = "Editar evento",
                            onClick = {navController.navigateToEditEvent(uiState.eventDetail!!.event.id.toString()) }
                        )
                    }
                }

                // Organizador con rol 2 - solo validación
                uiState.userOrganizerRole == 2 -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        QuickActionButton(
                            icon = Icons.Outlined.QrCodeScanner,
                            text = "Validar entradas",
                            onClick = {
                                navController.navigate(
                                    AppDestination.Scanner(
                                        eventId = uiState.eventDetail!!.event.id.toInt(),
                                        eventName = uiState.eventDetail!!.event.name
                                    )
                                )
                            }
                        )
                    }
                }

                // Usuario con ticket
                uiState.hasUserTicket -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        QuickActionButton(
                            icon = Icons.Outlined.QrCode,
                            text = "Mi entrada",
                            onClick = { /* Mostrar ticket */ }
                        )

                        QuickActionButton(
                            icon = Icons.Outlined.Share,
                            text = "Compartir",
                            onClick = { /* Compartir evento */ }
                        )

                        QuickActionButton(
                            icon = Icons.Outlined.LocationOn,
                            text = "Ubicación",
                            onClick = { }
                        )
                    }
                }

                // Usuario sin ticket
                else -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        QuickActionButton(
                            icon = Icons.Outlined.ShoppingCart,
                            text = "Comprar entrada",
                            onClick = { /* Ir a compra */ }
                        )

                        QuickActionButton(
                            icon = Icons.Outlined.Share,
                            text = "Compartir",
                            onClick = { /* Compartir evento */ }
                        )

                        QuickActionButton(
                            icon = Icons.Outlined.LocationOn,
                            text = "Ubicación",
                            onClick = { /* Mostrar mapa */ }
                        )
                    }
                }
            }
        }

        // Información adicional según el rol
        item {
            when {
                uiState.isOrganizer -> {
                    OrganizerInfoCard(uiState = uiState)
                }

                uiState.hasUserTicket -> {
                    UserTicketInfoCard(uiState = uiState)
                }

                else -> {
                    TicketPurchaseInfoCard(uiState = uiState)
                }
            }
        }
    }
}

@Composable
private fun OrganizerInfoCard(uiState: EventDetailUiState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {


                Icon(
                    imageVector = Icons.Outlined.AdminPanelSettings,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (uiState.isMainAdmin) "Administrador Principal" else "Organizador",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (uiState.isMainAdmin) {
                    "Tienes control total sobre este evento. Puedes gestionar organizadores, tipos de entrada y eliminar el evento."
                } else if (uiState.userOrganizerRole == 2) {
                    "Puedes validar entradas en la puerta del evento."
                } else {
                    "Puedes ayudar con la organización del evento."
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
private fun UserTicketInfoCard(uiState: EventDetailUiState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.ConfirmationNumber,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Entrada confirmada",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Ya tienes tu entrada para este evento. Recuerda llevar tu código QR el día del evento.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )

            if (uiState.userTicket != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Número de entrada: ${uiState.userTicket.entryNumber}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

@Composable
private fun TicketPurchaseInfoCard(uiState: EventDetailUiState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.7f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.ShoppingCart,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Entradas disponibles",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            val availableTickets = uiState.ticketTypes.filter { it.available > 0 }
            if (availableTickets.isNotEmpty()) {
                Text(
                    text = "Hay ${availableTickets.size} tipos de entrada disponibles. Ve a la sección de entradas para comprar.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            } else {
                Text(
                    text = "No hay entradas disponibles en este momento.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }
    }
}
