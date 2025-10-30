// ui/components/PlanCard.kt
package com.crewup.myapplication.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.crewup.myapplication.R
import com.crewup.myapplication.models.Plan
import com.crewup.myapplication.models.PlanLocation
import com.crewup.myapplication.models.PlanUser

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PlanCard(
    plan: Plan,
    currentUserUid: String,
    onPlanClick: (String) -> Unit,
    onJoinClick: (String) -> Unit,
    onEditClick: (String) -> Unit,
    onDeleteClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isCreator = plan.createdBy?.uid == currentUserUid

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onPlanClick(plan.id) },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            // === IMAGEN DEL PLAN ===
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
                    .height(200.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            )

            Column(modifier = Modifier.padding(16.dp)) {
                // === TÍTULO ===
                Text(
                    text = plan.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(4.dp))

                // === UBICACIÓN ===
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color(0xFF0056B3),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = plan.location.name.ifBlank { "Ubicación no especificada" },
                        fontSize = 14.sp,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(Modifier.height(8.dp))

                // === TAGS ===
                TagsRow(tags = plan.tags)

                Spacer(Modifier.height(12.dp))

                // === FECHA Y HORA ===
                Text(
                    text = plan.getFormattedDateTime(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF0056B3)
                )

                Spacer(Modifier.height(12.dp))

                // === FILTROS: EDAD + GÉNERO ===
                FiltersRow(
                    minAge = plan.minAge,
                    maxAge = plan.maxAge,
                    gender = plan.gender
                )

                Spacer(Modifier.height(16.dp))

                // === ACCIONES: CREADOR O PARTICIPANTE ===
                if (isCreator) {
                    CreatorActionsRow(
                        onEditClick = { onEditClick(plan.id) },
                        onDeleteClick = { onDeleteClick(plan.id) }
                    )
                } else {
                    ParticipantActionsRow(
                        participants = plan.participants,
                        onJoinClick = { onJoinClick(plan.id) }
                    )
                }
            }
        }
    }
}

// === EXTENSIÓN: FORMATEAR FECHA ===
fun Plan.getFormattedDateTime(): String {
    val date = this.date?.toDate() ?: return "Fecha no disponible"
    val dateFormat = SimpleDateFormat("EEEE, d MMM yyyy", Locale("es", "CO"))
    val timeText = if (this.time.isNotBlank()) this.time else {
        SimpleDateFormat("h:mm a", Locale("es", "CO")).format(date)
    }
    return "${dateFormat.format(date).replaceFirstChar { it.uppercase() }} • $timeText"
}

// === TAGS ===
@Composable
private fun TagsRow(tags: List<String>) {
    if (tags.isEmpty()) return

    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        tags.take(3).forEach { tag ->
            AssistChip(
                onClick = { },
                label = { Text(tag, fontSize = 12.sp) },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = Color(0xFFF0F4F8),
                    labelColor = Color(0xFF1976D2)
                )
            )
        }
        if (tags.size > 3) {
            Text(
                text = "+${tags.size - 3}",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
}

// === FILTROS ===
@Composable
private fun FiltersRow(minAge: Int, maxAge: Int, gender: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (minAge > 0 && maxAge > 0) {
            FilterChip(
                selected = false,
                onClick = { },
                label = { Text("+$minAge", fontSize = 12.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = Color(0xFFE3F2FD),
                    labelColor = Color(0xFF1976D2)
                )
            )
        }

        FilterChip(
            selected = false,
            onClick = { },
            label = { Text(gender, fontSize = 12.sp) },
            colors = FilterChipDefaults.filterChipColors(
                containerColor = Color(0xFFF3E5F5),
                labelColor = Color(0xFF7B1FA2)
            )
        )
    }
}

// === ACCIONES DEL CREADOR ===
@Composable
private fun CreatorActionsRow(
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = onEditClick,
            modifier = Modifier.height(40.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0056B3),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text("Editar", fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }

        IconButton(onClick = onDeleteClick) {
            Icon(
                Icons.Default.Delete,
                contentDescription = "Eliminar plan",
                tint = Color.Red
            )
        }
    }
}

// === ACCIONES DEL PARTICIPANTE ===

@Composable
private fun ParticipantActionsRow(
    participants: List<PlanUser>,
    onJoinClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(0.5f)
        ) {
            participants.take(3).forEachIndexed { index, user ->
                AsyncImage(
                    model = user.photoUrl.ifBlank { null },
                    contentDescription = null,
                    placeholder = painterResource(R.drawable.icon_profile),
                    error = painterResource(R.drawable.icon_profile),
                    modifier = Modifier
                        .size(32.dp)
                        .offset(x = -(index * 12).dp)
                        .clip(CircleShape)
                        .background(Color.LightGray),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(Modifier.width(8.dp))
            Text(
                text = "${participants.size} Participantes",
                fontSize = 14.sp,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // --- DERECHA: Botones (Chat + Unirme) ---
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* Abrir chat */ }) {
                Icon(
                    Icons.Default.ChatBubbleOutline,
                    contentDescription = "Chat",
                    tint = Color(0xFF0056B3)
                )
            }

            Button(
                onClick = onJoinClick,
                modifier = Modifier.height(40.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0056B3),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text("Unirme", fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}


@Preview(name = "Creador del Plan", showBackground = true)
@Composable
fun PlanCardCreatorPreview() {
    MaterialTheme {
        PlanCard(
            plan = mockPlanCreator,
            currentUserUid = "user123",
            onPlanClick = {},
            onJoinClick = {},
            onEditClick = {},
            onDeleteClick = {}
        )
    }
}

@Preview(name = "Participante Externo", showBackground = true)
@Composable
fun PlanCardParticipantPreview() {
    MaterialTheme {
        PlanCard(
            plan = mockPlanParticipant,
            currentUserUid = "otherUser456",
            onPlanClick = {},
            onJoinClick = {},
            onEditClick = {},
            onDeleteClick = {}
        )
    }
}

// === MOCK DATA PARA PREVIEW ===
private val mockPlanCreator = Plan(
    id = "plan123",
    title = "Intercambio de idiomas y juegos de mesa",
    description = "Practica inglés sin presión, con charlas y juegos.",
    imageUrl = "https://example.com/plan.jpg",
    location = PlanLocation(name = "Vintrash Bar Bogotá"),
    date = Timestamp.now(),
    time = "7:00 PM",
    tags = listOf("Idiomas", "Cantar", "Juegos"),
    minAge = 18,
    maxAge = 27,
    gender = "Todos",
    createdBy = PlanUser(
        uid = "user123",
        name = "Sneider",
        lastName = "Smith",
        photoUrl = "",
        city = "Bogotá"
    ),
    participants = listOf(
        PlanUser(uid = "user123", name = "Sneider", photoUrl = ""),
        PlanUser(uid = "p1", name = "Ana", photoUrl = ""),
        PlanUser(uid = "p2", name = "Luis", photoUrl = "")
    )
)

private val mockPlanParticipant = mockPlanCreator.copy(
    createdBy = PlanUser(uid = "creator789", name = "Pedro", lastName = "Gómez"),
    participants = listOf(
        PlanUser(uid = "creator789", name = "Pedro"),
        PlanUser(uid = "p1", name = "Ana"),
        PlanUser(uid = "p2", name = "Luis"),
        PlanUser(uid = "p3", name = "María")
    )
)