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
import com.crewup.myapplication.models.User

/**
 * Componente de encabezado del chat.
 * Muestra 3 avatares debajo del título, con fotos reales.
 *
 * @param planTitle Título del plan
 * @param participantCount Cantidad total de participantes (incluye creador)
 * @param participants Lista de usuarios participantes (máximo 3 para mostrar)
 * @param onBackClick Callback para volver
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
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ParticipantAvatarsSmall(participants = participants.take(5))

                    // AUMENTADO: 16.dp para más separación
                    Spacer(modifier = Modifier.width(30.dp))

                    // Bolita negra
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(28.dp)
                            .background(Color.Black, CircleShape)
                    ) {
                        Text(
                            text = if (participantCount > 10) "10+" else participantCount.toString(),
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

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

@Composable
private fun ParticipantAvatarsSmall(participants: List<PlanUser>) {
    Box {
        participants.forEachIndexed { index, user ->
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
    }
}

// ==================== PREVIEWS (FUNCIONALES) ====================

@Preview(name = "Header con 3 participantes", showBackground = true)
@Composable
private fun ChatHeaderPreview() {
    MaterialTheme {
        ChatHeader(
            planTitle = "Paintball",
            participantCount = 5,
            onBackClick = { }
        )
    }
}

@Preview(name = "Header con 12 participantes", showBackground = true)
@Composable
private fun ChatHeaderManyPreview() {
    MaterialTheme {
        ChatHeader(
            planTitle = "Partido de fútbol",
            participantCount = 12,
            onBackClick = { }
        )
    }
}