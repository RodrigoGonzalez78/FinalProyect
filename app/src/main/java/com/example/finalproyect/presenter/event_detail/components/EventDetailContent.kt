package com.example.finalproyect.presenter.event_detail.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.finalproyect.presenter.event_detail.EventDetailUiState
import com.example.finalproyect.presenter.event_detail.components.notification_section.NotificationsSection


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventDetailContent(
    navController: NavHostController,
    uiState: EventDetailUiState,
    activeSection: String,
    paddingValues: PaddingValues,
    onSectionChange: (String) -> Unit,
    onPurchaseTicket: (Int) -> Unit = {},
    onDeleteEvent: () -> Unit = {},
    onValidateTicket: (String) -> Unit = {},
    onCreateTicketType: (String, Double, String?, Int) -> Unit = { _, _, _, _ -> },
    onUpdateTicketType: (Int, String, Double, String?, Int) -> Unit = { _, _, _, _, _ -> },
    onCreateOrganizer: (String, Int) -> Unit = { _, _ -> },
    onUpdateOrganizerRole: (Int, Int) -> Unit = { _, _ -> },
    onDeleteOrganizer: (Int) -> Unit = {},
    onDeleteTicketType: (Int) -> Unit = {}
) {
    val eventDetail = uiState.eventDetail ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        // Banner del evento
        EventBanner(
            eventDetail = eventDetail,
            uiState = uiState,
            onDeleteEvent = onDeleteEvent
        )

        // Navegación por tabs
        EventNavigationTabs(
            uiState = uiState,
            activeSection = activeSection,
            onSectionChange = onSectionChange
        )

        // Contenido según la sección activa
        Box(modifier = Modifier.fillMaxSize()) {
            when (activeSection) {
                "overview" -> EventOverviewSection(
                    uiState = uiState,
                    onValidateTicket = onValidateTicket,
                    navController = navController
                )

                "tickets" -> {
                    if (!(uiState.isMainAdmin || uiState.userOrganizerRole == 2)) {
                        UserTicketSection(
                            uiState = uiState,
                            onPurchaseTicket = onPurchaseTicket
                        )
                    }
                }

                "notifications" -> NotificationsSection(uiState = uiState)
                "organizers" -> OrganizersSection(
                    uiState = uiState,
                    onUpdateOrganizerRole = onUpdateOrganizerRole,
                    onDeleteOrganizer = onDeleteOrganizer
                )

                "ticketTypes" -> TicketTypesSection(
                    uiState = uiState,
                    onUpdateTicketType = onUpdateTicketType,
                    onDeleteTicketType = onDeleteTicketType
                )
            }
        }
    }
}

