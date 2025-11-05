package com.crewup.myapplication.ui.components.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.crewup.myapplication.R
import com.crewup.myapplication.models.GroupMessage
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

/**
 * Componente que muestra un mensaje individual en el chat.
 * Separado para facilitar la personalización del diseño.
 *
 * @param message El mensaje a mostrar
 * @param isCurrentUser Si el mensaje es del usuario actual
 * @param onLongPress Callback cuando se mantiene presionado el mensaje
 */
@Composable
fun MessageBubble(
    message: GroupMessage,
    isCurrentUser: Boolean,
    onLongPress: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        // Avatar solo para mensajes de otros usuarios
        if (!isCurrentUser) {
            val photoUrl = message.userPhotoUrl
            val hasPhoto = !photoUrl.isNullOrBlank()

            Image(
                painter = rememberAsyncImagePainter(
                    model = if (hasPhoto) photoUrl else R.drawable.ic_pizza_logo_playstore,
                    placeholder = rememberAsyncImagePainter(R.drawable.ic_pizza_logo_playstore),
                    error = rememberAsyncImagePainter(R.drawable.ic_pizza_logo_playstore)
                ),
                contentDescription = "Avatar de ${message.userName}",
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            horizontalAlignment = if (isCurrentUser) Alignment.End else Alignment.Start
        ) {
            // Nombre del usuario (solo para otros)
            if (!isCurrentUser) {
                Text(
                    text = message.userName,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFAAAAAA),
                    modifier = Modifier.padding(start = 4.dp)
                )
                Spacer(modifier = Modifier.height(2.dp))
            }

            // Burbuja del mensaje
            Surface(
                modifier = Modifier.widthIn(max = 280.dp),
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = if (isCurrentUser) 16.dp else 4.dp,
                    bottomEnd = if (isCurrentUser) 4.dp else 16.dp
                ),
                color = if (isCurrentUser) Color(0xFF165BB0) else Color(0xFF434343),
                onClick = onLongPress
            ) {
                Box(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                    Text(
                        text = message.message,
                        color = if (isCurrentUser) Color.White else Color.White,
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 15.sp,
                        lineHeight = 20.sp
                    )
                }
            }

            // Timestamp debajo de la burbuja
            Text(
                text = formatTimestamp(message.timestamp.toDate()),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                color = Color(0xFF888888),
                modifier = Modifier
                    .padding(
                        top = 2.dp,
                        start = if (!isCurrentUser) 4.dp else 0.dp,
                        end = if (isCurrentUser) 4.dp else 0.dp
                    )
            )
        }
    }
}

/** Formatea el timestamp para mostrar solo la hora. */
private fun formatTimestamp(date: Date): String {
    val formatter = SimpleDateFormat("h:mm a", Locale.getDefault())
    return formatter.format(date).lowercase().replace(".", "")
}

// ==================== PREVIEWS (100% FUNCIONALES) ====================

@Preview(name = "Mensaje propio", showBackground = true)
@Composable
private fun MessageBubbleCurrentUserPreview() {
    MaterialTheme {
        Surface(color = Color(0xFFF5F5F5)) {
            Column(modifier = Modifier.padding(16.dp)) {
                MessageBubble(
                    message = GroupMessage(
                        id = "1",
                        userId = "currentUser",
                        userName = "Tú",
                        userPhotoUrl = "https://randomuser.me/api/portraits/women/44.jpg",
                        message = "Hola, Si Antes De Vernos En El Juego.",
                        timestamp = Timestamp.now()
                    ),
                    isCurrentUser = true
                )
            }
        }
    }
}

@Preview(name = "Mensaje de otro usuario", showBackground = true)
@Composable
private fun MessageBubbleOtherUserPreview() {
    MaterialTheme {
        Surface(color = Color(0xFFF5F5F5)) {
            Column(modifier = Modifier.padding(16.dp)) {
                MessageBubble(
                    message = GroupMessage(
                        id = "2",
                        userId = "otherUser",
                        userName = "Ana López",
                        userPhotoUrl = "https://randomuser.me/api/portraits/women/44.jpg",
                        message = "Hola A Todos, Quisiera Conocerlos Un Poco Más :).",
                        timestamp = Timestamp.now()
                    ),
                    isCurrentUser = false
                )
            }
        }
    }
}

@Preview(name = "Mensaje largo", showBackground = true)
@Composable
private fun MessageBubbleLongTextPreview() {
    MaterialTheme {
        Surface(color = Color(0xFFF5F5F5)) {
            Column(modifier = Modifier.padding(16.dp)) {
                MessageBubble(
                    message = GroupMessage(
                        id = "3",
                        userId = "otherUser",
                        userName = "María González",
                        userPhotoUrl = "https://randomuser.me/api/portraits/women/44.jpg",
                        message = "Este es un mensaje mucho más largo para ver cómo se comporta el componente cuando el texto ocupa múltiples líneas. Debería verse bien formateado y ser fácil de leer.",
                        timestamp = Timestamp.now()
                    ),
                    isCurrentUser = false
                )
            }
        }
    }
}

@Preview(name = "Mensaje sin foto de perfil", showBackground = true)
@Composable
private fun MessageBubbleNoPhotoPreview() {
    MaterialTheme {
        Surface(color = Color(0xFFF5F5F5)) {
            Column(modifier = Modifier.padding(16.dp)) {
                MessageBubble(
                    message = GroupMessage(
                        id = "4",
                        userId = "otherUser",
                        userName = "Pedro",
                        userPhotoUrl = "", // Sin foto
                        message = "¡Hola! No tengo foto de perfil.",
                        timestamp = Timestamp.now()
                    ),
                    isCurrentUser = false
                )
            }
        }
    }
}