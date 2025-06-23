package com.example.finalproyect.presenter.event_detail.components


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.finalproyect.presenter.event_detail.EventDetailUiState

@Composable
fun EventNavigationTabs(
    uiState: EventDetailUiState,
    activeSection: String,
    onSectionChange: (String) -> Unit
) {
    val tabs = buildList {
        // Tab general - siempre visible
        add(TabItem("overview", "General", Icons.Outlined.Dashboard))

        // Tab de tickets - diferente comportamiento según rol
        if (uiState.isMainAdmin || uiState.userOrganizerRole == 2) {
            add(TabItem("tickets", "Validación", Icons.Outlined.QrCodeScanner))
        } else {
            add(TabItem("tickets", "Mi Entrada", Icons.Outlined.ConfirmationNumber))
        }
        // Tabs solo para organizadores
        if (uiState.isOrganizer) {
            add(TabItem("notifications", "Avisos", Icons.Outlined.Notifications))
            add(TabItem("organizers", "Equipo", Icons.Outlined.People))

            // Solo admin principal y admin pueden gestionar tipos de ticket
            if (uiState.canManageTicketTypes) {
                add(TabItem("ticketTypes", "Tipos", Icons.Outlined.Category))
            }
        }
    }

    ScrollableTabRow(
        selectedTabIndex = tabs.indexOfFirst { it.key == activeSection }.takeIf { it >= 0 } ?: 0,
        edgePadding = 16.dp,
        containerColor = MaterialTheme.colorScheme.surface,
        divider = {}
    ) {
        tabs.forEach { tab ->
            Tab(
                selected = activeSection == tab.key,
                onClick = { onSectionChange(tab.key) },
                text = { Text(tab.title) },
                icon = { Icon(tab.icon, contentDescription = null) }
            )
        }
    }
}

private data class TabItem(
    val key: String,
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)
