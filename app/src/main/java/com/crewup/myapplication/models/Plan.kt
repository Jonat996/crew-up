package com.crewup.myapplication.models

import com.google.firebase.Timestamp

/**
 * Modelo de datos completo para un plan en Firestore.
 *
 * @param id ID único del plan (generado por Firestore)
 * @param title Título del plan
 * @param description Descripción detallada del plan
 * @param imageUrl URL de la imagen en Firebase Storage
 * @param location Ubicación del plan
 * @param date Fecha del plan como Timestamp de Firebase
 * @param time Hora del plan en formato de texto (ej: "7:00 PM")
 * @param tags Lista de etiquetas/categorías del plan
 * @param creatorUid UID del usuario creador del plan
 * @param minAge Edad mínima para unirse al plan
 * @param maxAge Edad máxima para unirse al plan
 * @param gender Género permitido ("M", "F", "Todos")
 * @param createdAt Timestamp de creación del plan
 */
data class Plan(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val location: PlanLocation = PlanLocation(),
    val date: Timestamp? = null,
    val time: String = "",
    val tags: List<String> = emptyList(),
    val creatorUid: String = "",
    val minAge: Int = 18,
    val maxAge: Int = 65,
    val gender: String = "Todos",
    val createdAt: Timestamp = Timestamp.now()
)
