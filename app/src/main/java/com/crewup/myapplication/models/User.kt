package com.crewup.myapplication.models

data class User(
    val uid: String = "",
    val email: String = "",
    val name: String = "",
    val lastName: String = "",
    val occupation: String = "",  // Ocupación
    val gender: String = "",      // Género (e.g., "Masculino", "Femenino", "Otro")
    val city: String = "",        // Ciudad
    val country: String = "",     // País (opcional)
    val phoneNumber: String = "", // Otros campos
    val photoUrl: String = ""     // URL de la foto de perfil
)
