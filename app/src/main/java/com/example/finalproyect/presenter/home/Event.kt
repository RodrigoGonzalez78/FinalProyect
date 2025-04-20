package com.example.finalproyect.presenter.home


import java.util.*

data class Event(
    val idEvent: Long,
    val idLocation: Long,
    val date: Date,
    val name: String,
    val description: String? = null,
    val banner: String? = null,
    val guestsNumber: Int = 0,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date(),
    val deletedAt: Date? = null,
    // Campo adicional para mostrar el nombre de la ubicación
    val locationName: String? = null
)

// Datos de ejemplo para la aplicación
val sampleEvents = listOf(
    Event(
        idEvent = 1,
        idLocation = 101,
        date = Calendar.getInstance().apply {
            set(2025, 4, 15, 20, 0)
        }.time,
        name = "Concierto de Jazz Nocturno",
        description = "Una noche de jazz con los mejores músicos de la ciudad. Disfruta de bebidas y buena música en un ambiente íntimo.",
        banner = "https://via.placeholder.com/400x200",
        guestsNumber = 120,
        locationName = "Club Blue Note"
    ),
    Event(
        idEvent = 2,
        idLocation = 102,
        date = Calendar.getInstance().apply {
            set(2025, 4, 20, 19, 30)
        }.time,
        name = "Festival de Cine Independiente",
        description = "Proyección de cortometrajes y películas independientes con sesión de preguntas y respuestas con los directores.",
        banner = "https://via.placeholder.com/400x200",
        guestsNumber = 85,
        locationName = "Teatro Lumière"
    ),
    Event(
        idEvent = 3,
        idLocation = 103,
        date = Calendar.getInstance().apply {
            set(2025, 5, 1, 21, 0)
        }.time,
        name = "Exposición de Arte Digital",
        description = "Muestra de arte digital interactivo con obras de artistas emergentes y establecidos.",
        banner = "https://via.placeholder.com/400x200",
        guestsNumber = 150,
        locationName = "Galería Nova"
    )
)
