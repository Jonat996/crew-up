package com.crewup.myapplication.ui.components.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.crewup.myapplication.R
import com.crewup.myapplication.models.PlanUser

/**
 * Encabezado del chat con 4 avatares + bolita solo si >4 participantes
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatHeader(
    planTitle: String,
    participantCount: Int,
    participants: List<PlanUser> = emptyList(),
    onBackClick: () -> Unit
) {
    TopAppBar(
        modifier = Modifier.background(Color(0xFF165BB0)),
        title = {
            Column {
                Text(
                    text = planTitle,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    ParticipantAvatarsWithCount(
                        participants = participants,
                        totalCount = participantCount
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.chat_back),
                    tint = Color.White
                )
            }
        },
        actions = {
            IconButton(onClick = { }) {
                Icon(Icons.Default.Notifications, contentDescription = "Notif", tint = Color.White)
            }
            IconButton(onClick = { }) {
                Icon(Icons.Default.MoreVert, contentDescription = "Más", tint = Color.White)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF165BB0),
            titleContentColor = Color.White
        )
    )
}

/**
 * 4 avatares + bolita negra solo si hay más de 4 participantes
 */
@Composable

private fun ParticipantAvatarsWithCount(
    participants: List<PlanUser>,
    totalCount: Int
) {
    Box {
        // Mostrar hasta 4 avatares
        val visibleAvatars = participants.take(4)
        val showCountBubble = totalCount > 4
        val extraCount = totalCount - 5  // <--- AQUÍ RESTAMOS 1 (porque estás contando uno de más)

        visibleAvatars.forEachIndexed { index, user ->
            Image(
                painter = rememberAsyncImagePainter(
                    model = user.photoUrl ?: "https://ui-avatars.com/api/?name=${user.name}&background=ffffff&color=165BB0&size=32",
                    placeholder = rememberAsyncImagePainter("https://ui-avatars.com/api/?name=${user.name}&background=ffffff&color=165BB0&size=32")
                ),
                contentDescription = "Avatar de ${user.name}",
                modifier = Modifier
                    .size(28.dp)
                    .offset(x = (index * 16).dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        if (showCountBubble && extraCount > 0) {
            val lastAvatarOffset = (visibleAvatars.size - 1) * 16
            Box(
                modifier = Modifier
                    .offset(x = (lastAvatarOffset + 20).dp)
                    .size(28.dp)
                    .background(Color.Black, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (extraCount >= 10) "10+" else "+$extraCount",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

// ==================== PREVIEWS ====================

@Preview(name = "1 participante")
@Composable
private fun Preview1() {
    ChatHeader(
        planTitle = "Café",
        participantCount = 1,
        participants = listOf(PlanUser("1", "Ana")),
        onBackClick = {}
    )
}

@Preview(name = "4 participantes")
@Composable
private fun Preview4() {
    ChatHeader(
        planTitle = "Cine",
        participantCount = 4,
        participants = List(4) { PlanUser("$it", "User $it") },
        onBackClick = {}
    )
}

@Preview(name = "5 participantes (+1)")
@Composable
private fun Preview5() {
    ChatHeader(
        planTitle = "Paintball",
        participantCount = 5,
        participants = List(5) { PlanUser("$it", "User $it") },
        onBackClick = {}
    )
}

@Preview(name = "8 participantes (+4)")
@Composable
private fun Preview8() {
    ChatHeader(
        planTitle = "Cena",
        participantCount = 8,
        participants = List(8) { PlanUser("$it", "User $it") },
        onBackClick = {}
    )
}

@Preview(name = "10 participantes (10+)")
@Composable
private fun Preview10() {
    ChatHeader(
        planTitle = "Viaje",
        participantCount = 10,
        participants = List(10) { PlanUser("$it", "User $it") },
        onBackClick = {}
    )
}

@Preview(name = "15 participantes (10+)")
@Composable
private fun Preview15() {
    ChatHeader(
        planTitle = "Fiesta",
        participantCount = 15,
        participants = List(15) { PlanUser("$it", "User $it") },
        onBackClick = {}
    )
}