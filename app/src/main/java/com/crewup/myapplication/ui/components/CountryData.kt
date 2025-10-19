package com.crewup.myapplication.ui.components

// ----------------------
// MODELO DE DATOS
// ----------------------
data class CountryStructure(
    val name: String,
    val code: String,
    val flagUrl: String,
    val cities: List<String>
)

// ----------------------
// LISTA DE PAÍSES
// ----------------------
val countriesList = listOf(
    CountryStructure(
        name = "Colombia",
        code = "+57",
        flagUrl = "https://flagcdn.com/w320/co.png",
        cities = listOf("Bogotá D.C.", "Medellín", "Cali", "Barranquilla")
    ),
    CountryStructure(
        name = "México",
        code = "+52",
        flagUrl = "https://flagcdn.com/w320/mx.png",
        cities = listOf("Ciudad de México", "Guadalajara", "Monterrey")
    ),
    CountryStructure(
        name = "Argentina",
        code = "+54",
        flagUrl = "https://flagcdn.com/w320/ar.png",
        cities = listOf("Buenos Aires", "Córdoba", "Rosario")
    ),
    CountryStructure(
        name = "Peru",
        code = "+51",
        flagUrl = "https://flagcdn.com/w320/pe.png",
        cities = listOf("Lima")
    )
)
