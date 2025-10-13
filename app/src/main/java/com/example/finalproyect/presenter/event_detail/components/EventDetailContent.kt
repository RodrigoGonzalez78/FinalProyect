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
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.finalproyect.presenter.event_detail.EventDetailUiState
import com.example.finalproyect.presenter.event_detail.EventDetailsViewModel
import com.example.finalproyect.presenter.event_detail.components.notification_section.NotificationsSection


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventDetailContent(
    navController: NavHostController,
    uiState: EventDetailUiState,
    activeSection: String,
    paddingValues: PaddingValues,
    viewModel: EventDetailsViewModel
) {
    val eventDetail = uiState.eventDetail ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {

        EventBanner(
            eventDetail = eventDetail,
            uiState = uiState,
            onDeleteEvent = { viewModel.deleteEvent() }

        )


        EventNavigationTabs(
            uiState = uiState,
            activeSection = activeSection,
            onSectionChange = { section -> viewModel.setActiveSection(section) }
        )

        Box(modifier = Modifier.fillMaxSize()) {
            when (activeSection) {
                "overview" -> EventOverviewSection(
                    uiState = uiState,
                    navController = navController
                )

                "notifications" -> NotificationsSection(uiState = uiState, viewModel)
                "organizers" -> OrganizersSection(
                    uiState = uiState,
                    onUpdateOrganizerRole = {organizerId,rolId->viewModel.updateOrganizerRole(organizerId,rolId)},
                    onDeleteOrganizer = {idOrganizer-> viewModel.deleteOrganizer(idOrganizer)}
                )

                "ticketTypes" -> TicketTypesSection(
                    uiState = uiState,
                    navController
                )
            }
        }
    }
}

