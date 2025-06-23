package com.example.finalproyect.presenter.event_detail.components

import com.example.finalproyect.domain.model.OrganizerRole
import com.example.finalproyect.domain.model.TicketType
import com.example.finalproyect.presenter.event_detail.EventDetailUiState

// Extensiones para facilitar el uso en la UI
fun EventDetailUiState.getAvailableRoles(): List<OrganizerRole> {
    return OrganizerRole.getAvailableRoles()
}

fun EventDetailUiState.canPurchaseTickets(): Boolean {
    return !hasUserTicket && ticketTypes.any { it.available > 0 }
}

fun EventDetailUiState.getAvailableTicketTypes(): List<TicketType> {
    return ticketTypes.filter { it.available > 0 }
}

fun EventDetailUiState.isAnyOperationInProgress(): Boolean {
    return isLoading ||
            isLoadingTicketTypes ||
            isLoadingOrganizers ||
            isLoadingUserTicket ||
            isCreatingTicketType ||
            isUpdatingTicketType ||
            isDeletingTicketType ||
            isCreatingOrganizer ||
            isUpdatingOrganizer ||
            isDeletingOrganizer ||
            isPurchasingTicket ||
            isDeletingEvent
}