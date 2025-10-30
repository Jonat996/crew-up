// ui/components/sections/plans/PlanDetailCard.kt
package com.crewup.myapplication.ui.components.sections.plans

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.crewup.myapplication.R
import com.crewup.myapplication.models.Plan
import com.crewup.myapplication.models.PlanUser
import com.crewup.myapplication.ui.components.getFormattedDateTime

/**
 * Card expandida que muestra el detalle completo de un plan.
 *
 * Incluye:
 * - Imagen más grande (300dp)
 * - Descripción completa del plan
 * - Información de ubicación, fecha, filtros
 * - Botones de acción según el estado del usuario
 * - Botón de cerrar en la esquina superior derecha
 */
@Composable
fun PlanDetailCard(
    plan: Plan,
    currentUserUid: String,
    onJoinClick: (String) -> Unit,
    onLeaveClick: (String) -> Unit,
    onChatClick: (String) -> Unit,
    onEditClick: (String) -> Unit,
    onDeleteClick: (String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isCreator = plan.createdBy?.uid == currentUserUid
    val isJoined = plan.participants.any { it.uid == currentUserUid }
    val scrollState = rememberScrollState()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.95f),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                // IMAGEN MÁS GRANDE
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(plan.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = plan.title,
                    placeholder = painterResource(R.drawable.ic_map_plan),
                    error = painterResource(R.drawable.ic_map_plan),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                )

                Column(modifier = Modifier.padding(20.dp)) {
                    // TÍTULO
                    Text(
                        text = plan.title,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0056B3)
                    )

                    Spacer(Modifier.height(8.dp))

                    // UBICACIÓN
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = Color(0xFF0056B3),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = plan.location.name.ifBlank { "Ubicación no especificada" },
                            fontSize = 16.sp,
                            color = Color.Gray,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Dirección completa si existe
                    if (plan.location.fullAddress.isNotBlank()) {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = plan.location.fullAddress,
                            fontSize = 14.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(start = 24.dp)
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    // DESCRIPCIÓN
                    Text(
                        text = "Descripción",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0056B3)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = plan.description.ifBlank { "Sin descripción" },
                        fontSize = 15.sp,
                        color = Color.DarkGray,
                        lineHeight = 22.sp
                    )

                    Spacer(Modifier.height(16.dp))

                    // TAGS
                    if (plan.tags.isNotEmpty()) {
                        Text(
                            text = "Categorías",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0056B3)
                        )
                        Spacer(Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            plan.tags.forEach { tag ->
                                AssistChip(
                                    onClick = { },
                                    label = { Text(tag, fontSize = 13.sp) },
                                    colors = AssistChipDefaults.assistChipColors(
                                        containerColor = Color(0xFFF0F4F8),
                                        labelColor = Color(0xFF1976D2)
                                    )
                                )
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                    }

                    // FECHA Y HORA
                    Text(
                        text = "Fecha y hora",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0056B3)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = plan.getFormattedDateTime(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.DarkGray
                    )

                    Spacer(Modifier.height(16.dp))

                    // FILTROS
                    Text(
                        text = "Requisitos",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0056B3)
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (plan.minAge > 0 && plan.maxAge > 0) {
                            FilterChip(
                                selected = false,
                                onClick = { },
                                label = { Text("Edad: ${plan.minAge}-${plan.maxAge}", fontSize = 13.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = Color(0xFFE3F2FD),
                                    labelColor = Color(0xFF1976D2)
                                )
                            )
                        }
                        FilterChip(
                            selected = false,
                            onClick = { },
                            label = { Text("Género: ${plan.gender}", fontSize = 13.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = Color(0xFFF3E5F5),
                                labelColor = Color(0xFF7B1FA2)
                            )
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    // PARTICIPANTES
                    Text(
                        text = "Participantes (${plan.participants.size})",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0056B3)
                    )
                    Spacer(Modifier.height(12.dp))
                    ParticipantsList(participants = plan.participants)

                    Spacer(Modifier.height(24.dp))

                    // BOTONES DE ACCIÓN
                    ActionButtons(
                        isCreator = isCreator,
                        isJoined = isJoined,
                        onJoinClick = { onJoinClick(plan.id) },
                        onLeaveClick = { onLeaveClick(plan.id) },
                        onChatClick = { onChatClick(plan.id) },
                        onEditClick = { onEditClick(plan.id) },
                        onDeleteClick = { onDeleteClick(plan.id) }
                    )

                    Spacer(Modifier.height(16.dp))
                }
            }

            // BOTÓN CERRAR (esquina superior derecha)
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .background(Color.White.copy(alpha = 0.9f), CircleShape)
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Cerrar",
                    tint = Color(0xFF0056B3)
                )
            }
        }
    }
}

@Composable
private fun ParticipantsList(participants: List<PlanUser>) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        participants.forEach { participant ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    model = participant.photoUrl.ifBlank { null },
                    contentDescription = null,
                    placeholder = painterResource(R.drawable.icon_profile),
                    error = painterResource(R.drawable.icon_profile),
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${participant.name} ${participant.lastName}".trim(),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (participant.occupation.isNotBlank()) {
                        Text(
                            text = participant.occupation,
                            fontSize = 13.sp,
                            color = Color.Gray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ActionButtons(
    isCreator: Boolean,
    isJoined: Boolean,
    onJoinClick: () -> Unit,
    onLeaveClick: () -> Unit,
    onChatClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    when {
        isCreator -> {
            // Botones para el creador
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = onEditClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF0056B3)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Edit, "Editar", modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Editar plan", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                }
                OutlinedButton(
                    onClick = onDeleteClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                    border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.5.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Delete, "Eliminar", tint = Color.Red, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Eliminar plan", fontSize = 16.sp, color = Color.Red, fontWeight = FontWeight.Medium)
                }
            }
        }
        isJoined -> {
            // Botones para participantes
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = onChatClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF0056B3)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.ChatBubbleOutline, "Chat", modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Ir al chat", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                }
                OutlinedButton(
                    onClick = onLeaveClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                    border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.5.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.ExitToApp, "Salir", tint = Color.Red, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Salir del plan", fontSize = 16.sp, color = Color.Red, fontWeight = FontWeight.Medium)
                }
            }
        }
        else -> {
            // Botón para usuarios no unidos
            Button(
                onClick = onJoinClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF0056B3)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Unirme al plan", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}
