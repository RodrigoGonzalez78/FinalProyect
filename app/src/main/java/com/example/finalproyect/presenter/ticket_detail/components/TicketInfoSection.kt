package com.example.finalproyect.presenter.ticket_detail.components


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.finalproyect.domain.model.Ticket
import com.example.finalproyect.domain.model.TicketType

@Composable
fun TicketInfoSection(
    ticket: Ticket,
    ticketType: TicketType?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)
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
                    imageVector = Icons.Outlined.ConfirmationNumber,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Información del Ticket",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Información del ticket
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Número de entrada
                TicketInfoRow(
                    icon = Icons.Outlined.Numbers,
                    label = "Número de entrada",
                    value = ticket.entryNumber
                )

                // Tipo de ticket
                if (ticketType != null) {
                    TicketInfoRow(
                        icon = Icons.Outlined.Category,
                        label = "Tipo de entrada",
                        value = ticketType.name
                    )
                }

                // Precio
                TicketInfoRow(
                    icon = Icons.Outlined.AttachMoney,
                    label = "Precio pagado",
                    value = ticket.formattedPrice,
                    valueColor = MaterialTheme.colorScheme.primary
                )

                // Fecha de compra
                TicketInfoRow(
                    icon = Icons.Outlined.Schedule,
                    label = "Fecha de compra",
                    value = ticket.createdAt.toString()
                )

                // Estado del ticket
                TicketInfoRow(
                    icon = if (ticket.isValid) Icons.Outlined.CheckCircle else Icons.Outlined.Error,
                    label = "Estado",
                    value = if (ticket.isValid) "Válido" else "Inválido",
                    valueColor = if (ticket.isValid)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun TicketInfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    valueColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = valueColor
        )
    }
}
