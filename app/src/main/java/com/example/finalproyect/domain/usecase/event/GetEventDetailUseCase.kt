package com.example.finalproyect.domain.usecase.event

import com.example.finalproyect.domain.model.EventDetail
import com.example.finalproyect.domain.repository.EventRepository
import com.example.finalproyect.utils.PreferenceManager
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetEventDetailUseCase @Inject constructor(
    private val eventRepository: EventRepository,
    private val preferenceManager: PreferenceManager
) {
    suspend operator fun invoke(eventId: Int): Pair<Result<EventDetail>, Boolean> {
        if (eventId <= 0) {
            return Pair(Result.failure(Exception("Invalid event ID")), false)
        }

        val eventDetail = eventRepository.getEventById(eventId)
        var isOrganizer: Boolean = false
        val idUser = preferenceManager.getCurrentUserID().first().toLong()
        for (i in eventDetail.getOrNull()?.organizers!!) {
            if (i.idUser == idUser) {
                isOrganizer = true
                break
            }

        }

        return Pair(eventDetail, isOrganizer)
    }
}