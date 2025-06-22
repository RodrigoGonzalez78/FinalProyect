package com.example.finalproyect.domain.repository

import com.example.finalproyect.domain.model.Organizer


interface OrganizerRepository {
    suspend fun createOrganizer(
        eventId: Int,
        userId: Int,
        roleId: Int
    ): Result<Organizer>

    suspend fun getOrganizersByEvent(eventId: Int): Result<List<Organizer>>

    suspend fun updateOrganizerRole(
        eventId: Int,
        organizerId: Int,
        newRoleId: Int
    ): Result<Organizer>

    suspend fun deleteOrganizer(
        eventId: Int,
        organizerId: Int
    ): Result<Unit>

    suspend fun getOrganizerById(organizerId: Int): Result<Organizer?>

    suspend fun isUserOrganizerOfEvent(eventId: Int, userId: Int): Result<Boolean>
}
