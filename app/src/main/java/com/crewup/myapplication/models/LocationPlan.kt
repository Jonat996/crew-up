package com.crewup.myapplication.models

// Modelo de datos para guardar la ubicación final en Firestore
data class PlanLocation(
    val id: String = "", // Usaremos el Place ID de Google Maps
    val name: String = "",
    val fullAddress: String = "",
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val city: String? = null, // Ciudad de la ubicación (nullable para compatibilidad)
    val country: String? = null // País de la ubicación (nullable para compatibilidad)
)