package com.example.finalproyect.presenter.event_detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.People
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.finalproyect.domain.model.EventDetail
import com.example.finalproyect.presenter.event_detail.components.notification_section.NotificationsSection
import com.example.finalproyect.presenter.event_detail.components.organizer_section.OrganizersSection
import com.example.finalproyect.presenter.event_detail.components.ticket_section.TicketsSection
import com.example.finalproyect.presenter.event_detail.components.ticket_type_section.TicketTypesSection
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun EventDetailContent(
    eventDetail: EventDetail,
    activeSection: String,
    paddingValues: PaddingValues,
    onSectionChange: (String) -> Unit
) {
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
                model = eventDetail.event.banner ?: "https://via.placeholder.com/800x400",
                contentDescription = eventDetail.event.name,
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
                    text = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale("es", "ES")).format(
                        eventDetail.event.date
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )

                Text(
                    text = eventDetail.location.name ?: "Sin ubicación",
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
               // "statistics" -> 4
                "ticketTypes" -> 5
                else -> 0
            },
            edgePadding = 16.dp,
            containerColor = MaterialTheme.colorScheme.surface,
            divider = {}
        ) {
            Tab(
                selected = activeSection == "overview",
                onClick = { onSectionChange("overview") },
                text = { Text("General") },
                icon = { Icon(Icons.Outlined.Dashboard, contentDescription = null) }
            )
            Tab(
                selected = activeSection == "tickets",
                onClick = { onSectionChange("tickets") },
                text = { Text("Entradas") },
                icon = { Icon(Icons.Outlined.ConfirmationNumber, contentDescription = null) }
            )
            Tab(
                selected = activeSection == "notifications",
                onClick = { onSectionChange("notifications") },
                text = { Text("Avisos") },
                icon = { Icon(Icons.Outlined.Notifications, contentDescription = null) }
            )
            Tab(
                selected = activeSection == "organizers",
                onClick = { onSectionChange("organizers") },
                text = { Text("Equipo") },
                icon = { Icon(Icons.Outlined.People, contentDescription = null) }
            )
            /*Tab(
                selected = activeSection == "statistics",
                onClick = { onSectionChange("statistics") },
                text = { Text("Estadísticas") },
                icon = { Icon(Icons.Outlined.BarChart, contentDescription = null) }
            )*/
            Tab(
                selected = activeSection == "ticketTypes",
                onClick = { onSectionChange("ticketTypes") },
                text = { Text("Tipos") },
                icon = { Icon(Icons.Outlined.Category, contentDescription = null) }
            )
        }

        // Contenido de la sección activa
        Box(modifier = Modifier.fillMaxSize()) {
            when (activeSection) {
                "overview" -> EventOverviewSection(eventDetail.event)
                "tickets" -> TicketsSection()
                "notifications" -> NotificationsSection()
                "organizers" -> OrganizersSection(eventDetail.organizers)
                "ticketTypes" -> TicketTypesSection()
            }
        }
    }
}