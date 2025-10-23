package com.crewup.myapplication.models

data class DetailPlan(
    val imageUrl: String,
    val title: String,
    val locationName: String,
    val tags: List<String>,
    val date: String, // Ej: "Jueves, 7:00 PM"
    val time: String, // Ej: "25 sep 2025"
)

// Datos de ejemplo para la simulación
val mockPlanDetail = DetailPlan(
    imageUrl = "URL_DE_LA_IMAGEN_DEL_BAR", // Simulación
    title = "Intercambio idiomas y\njuegos de mesa",
    locationName = "Vintrash Bar Bogotá",
    tags = listOf("Intercambio Idiomas", "Cantar", "Juegos de Mesa"),
    date = "Jueves, 7:00 PM",
    time = "25 sep 2025"
)
