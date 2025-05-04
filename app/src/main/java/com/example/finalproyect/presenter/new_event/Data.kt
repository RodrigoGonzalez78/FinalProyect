package com.example.finalproyect.presenter.new_event


data class Location(
    val id: Long,
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double
)

// Datos de ejemplo para ubicaciones
val sampleLocations = listOf(
    Location(
        id = 101,
        name = "Club Blue Note",
        address = "Av. Principal 123, Ciudad",
        latitude = 19.4326,
        longitude = -99.1332
    ),
    Location(
        id = 102,
        name = "Teatro Lumière",
        address = "Calle Cultural 45, Centro",
        latitude = 19.4287,
        longitude = -99.1359
    ),
    Location(
        id = 103,
        name = "Galería Nova",
        address = "Paseo de las Artes 78, Zona Moderna",
        latitude = 19.4240,
        longitude = -99.1673
    ),
    Location(
        id = 104,
        name = "Centro de Convenciones Stellar",
        address = "Blvd. Empresarial 200, Distrito Financiero",
        latitude = 19.4420,
        longitude = -99.1443
    ),
    Location(
        id = 105,
        name = "Jardín Botánico Orquídea",
        address = "Av. de las Flores 56, Parque Central",
        latitude = 19.4150,
        longitude = -99.1700
    )
)
