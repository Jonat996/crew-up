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
    onLeaveClick: (String) -> Unit,
    onChatClick: (String) -> Unit,
    onEditClick: (String) -> Unit,
    onDeleteClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isCreator = plan.createdBy?.uid == currentUserUid
    val isJoined = plan.participants.any { it.uid == currentUserUid }

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
            // IMAGEN
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
                // TÍTULO
                Text(
                    text = plan.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(4.dp))

                // UBICACIÓN
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
                        modifier = Modifier.weight(1f) // CORRECTO
                    )
                }

                Spacer(Modifier.height(8.dp))

                // TAGS
                TagsRow(tags = plan.tags)

                Spacer(Modifier.height(12.dp))

                // FECHA Y HORA
                Text(
                    text = plan.getFormattedDateTime(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF0056B3)
                )

                Spacer(Modifier.height(12.dp))

                // FILTROS
                FiltersRow(minAge = plan.minAge, maxAge = plan.maxAge, gender = plan.gender)

                Spacer(Modifier.height(16.dp))

                // PARTICIPANTES + ACCIONES
                ParticipantsAndActionsRow(
                    participants = plan.participants,
                    isCreator = isCreator,
                    isJoined = isJoined,
                    onJoinClick = { onJoinClick(plan.id) },
                    onLeaveClick = { onLeaveClick(plan.id) },
                    onChatClick = { onChatClick(plan.id) },
                    onEditClick = { onEditClick(plan.id) },
                    onDeleteClick = { onDeleteClick(plan.id) }
                )
            }
        }
    }
}

// === PARTICIPANTES + ACCIONES ===
@Composable
private fun ParticipantsAndActionsRow(
    participants: List<PlanUser>,
    isCreator: Boolean,
    isJoined: Boolean,
    onJoinClick: () -> Unit,
    onLeaveClick: () -> Unit,
    onChatClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ParticipantsAvatarRow(participants = participants)

        Row {
            when {
                isCreator -> {
                    Button(
                        onClick = onEditClick,
                        modifier = Modifier.height(40.dp),
                        colors = ButtonDefaults.buttonColors(Color(0xFF0056B3)),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text("Editar", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    }
                    Spacer(Modifier.width(1.dp))
                    IconButton(onClick = onDeleteClick) {
                        Icon(Icons.Default.Delete, "Eliminar", tint = Color.Red)
                    }
                }
                isJoined -> {
                    IconButton(onClick = onChatClick) {
                        Icon(Icons.Default.ChatBubbleOutline, "Chat", tint = Color(0xFF0056B3))
                    }
                    OutlinedButton(
                        onClick = onLeaveClick,
                        modifier = Modifier.height(40.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                        border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp)
                    ) {
                        Icon(Icons.Default.ExitToApp, "Salir", tint = Color.Red, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Salir", fontSize = 14.sp, color = Color.Red)
                    }
                }
                else -> {
                    Button(
                        onClick = onJoinClick,
                        modifier = Modifier.height(40.dp),
                        colors = ButtonDefaults.buttonColors(Color(0xFF0056B3)),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text("Unirme", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}

// === AVATARES + CONTADOR ===
@Composable
private fun ParticipantsAvatarRow(participants: List<PlanUser>) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(end = 12.dp)
    ) {
        participants.take(2).forEachIndexed { index, user ->
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
        Spacer(Modifier.width(3.dp))
        Text(
            text = "${participants.size} Participantes",
            fontSize = 14.sp,
            color = Color.Gray,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// === FORMATEO DE FECHA ===
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
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
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
            Text("+${tags.size - 3}", fontSize = 12.sp, color = Color.Gray)
        }
    }
}

// === FILTROS ===
@Composable
private fun FiltersRow(minAge: Int, maxAge: Int, gender: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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

// ================================================
// 3 PREVIEWS (TODOS CON PARTICIPANTES)
// ================================================

@Preview(name = "1. Creador", showBackground = true)
@Composable
fun PlanCardCreatorPreview() {
    MaterialTheme {
        PlanCard(
            plan = mockPlanCreator,
            currentUserUid = "user123",
            onPlanClick = {},
            onJoinClick = {},
            onLeaveClick = {},
            onChatClick = {},
            onEditClick = {},
            onDeleteClick = {}
        )
    }
}

@Preview(name = "2. No unido", showBackground = true)
@Composable
fun PlanCardNotJoinedPreview() {
    MaterialTheme {
        PlanCard(
            plan = mockPlanNotJoined,
            currentUserUid = "otherUser456",
            onPlanClick = {},
            onJoinClick = {},
            onLeaveClick = {},
            onChatClick = {},
            onEditClick = {},
            onDeleteClick = {}
        )
    }
}

@Preview(name = "3. Ya unido", showBackground = true)
@Composable
fun PlanCardJoinedPreview() {
    MaterialTheme {
        PlanCard(
            plan = mockPlanJoined,
            currentUserUid = "participant789",
            onPlanClick = {},
            onJoinClick = {},
            onLeaveClick = {},
            onChatClick = {},
            onEditClick = {},
            onDeleteClick = {}
        )
    }
}

// === MOCK DATA ===
private val mockPlanCreator = Plan(
    id = "plan1",
    title = "Intercambio idiomas y juegos",
    imageUrl = "https://example.com/plan.jpg",
    location = PlanLocation(name = "Vintrash Bar Bogotá"),
    date = Timestamp.now(),
    time = "7:00 PM",
    tags = listOf("Idiomas", "Juegos"),
    minAge = 18,
    gender = "Todos",
    createdBy = PlanUser(uid = "user123", name = "Sneider"),
    participants = listOf(
        PlanUser(uid = "user123", name = "Sneider"),
        PlanUser(uid = "p1", name = "Ana"),
        PlanUser(uid = "p2", name = "Luis")
    )
)

private val mockPlanNotJoined = mockPlanCreator.copy(
    createdBy = PlanUser(uid = "creator999", name = "Pedro"),
    participants = listOf(
        PlanUser(uid = "creator999", name = "Pedro"),
        PlanUser(uid = "p1", name = "Ana")
    )
)

private val mockPlanJoined = mockPlanCreator.copy(
    createdBy = PlanUser(uid = "creator999", name = "Pedro"),
    participants = listOf(
        PlanUser(uid = "creator999", name = "Pedro"),
        PlanUser(uid = "participant789", name = "Ana"),
        PlanUser(uid = "p2", name = "Luis"),
        PlanUser(uid = "p3", name = "María")
    )
)