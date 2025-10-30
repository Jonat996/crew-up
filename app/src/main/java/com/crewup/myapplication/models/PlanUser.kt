package com.crewup.myapplication.models

/**
 * Modelo de datos para representar un usuario dentro de un plan.
 * Usado para createdBy y participants.
 *
 * @param uid ID único del usuario
 * @param name Nombre del usuario
 * @param lastName Apellido del usuario
 * @param photoUrl URL de la foto de perfil
 * @param gender Género del usuario
 * @param occupation Ocupación del usuario
 * @param city Ciudad del usuario
 */
data class PlanUser(
    val uid: String = "",
    val name: String = "",
    val lastName: String = "",
    val photoUrl: String = "",
    val gender: String = "",
    val occupation: String = "",
    val city: String = ""
)
