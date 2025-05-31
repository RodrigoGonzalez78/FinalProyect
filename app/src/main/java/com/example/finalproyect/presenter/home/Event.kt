package com.example.finalproyect.presenter.home


import com.example.finalproyect.domain.model.Event
import java.util.*



val sampleEvents = listOf(
    Event(
        idEvent = 1,
        idLocation = 101,
        date = Calendar.getInstance().apply {
            set(2025, 4, 15, 20, 0)
        }.time,
        name = "Concierto de Jazz Nocturno",
        description = "Una noche de jazz con los mejores músicos de la ciudad. Disfruta de bebidas y buena música en un ambiente íntimo.",
        banner = "https://estaticosgn-cdn.deia.eus/clip/3ba45dff-d1fc-48dd-a12e-c1aa0cf4cfc1_16-9-aspect-ratio_default_0.jpg",
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
        banner = "https://i.ytimg.com/vi/753NbR_U3m4/maxresdefault.jpg",
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
        banner = "https://img.lalr.co/cms/2022/03/31124833/COLP_EXT_106936_553dc-1.jpg?size=xl",
        guestsNumber = 150,
        locationName = "Galería Nova"
    )
)
