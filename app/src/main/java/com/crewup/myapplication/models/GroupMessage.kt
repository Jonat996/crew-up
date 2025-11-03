package com.crewup.myapplication.models

import com.google.firebase.Timestamp

/**
 * Modelo de datos para un mensaje en el chat grupal de un plan.
 *
 * @param id ID único del mensaje
 * @param userId UID del usuario que envió el mensaje
 * @param userName Nombre completo del usuario
 * @param userPhotoUrl URL de la foto de perfil del usuario
 * @param message Contenido del mensaje
 * @param timestamp Fecha y hora del mensaje
 */
data class GroupMessage(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val userPhotoUrl: String = "",
    val message: String = "",
    val timestamp: Timestamp = Timestamp.now()
)
