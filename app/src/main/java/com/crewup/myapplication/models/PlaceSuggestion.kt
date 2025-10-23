package com.crewup.myapplication.models

// Modelo de datos para las sugerencias del Places Autocomplete
data class PlaceSuggestion(
    val placeId: String,
    val description: String,
    val secondaryText: String? = null  // e.g., "Ciudad, País"
)