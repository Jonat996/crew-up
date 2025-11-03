package com.crewup.myapplication.ui.components.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        Card(
            modifier = Modifier.widthIn(max = 280.dp),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isCurrentUser) 16.dp else 4.dp,
                bottomEnd = if (isCurrentUser) 4.dp else 16.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = if (isCurrentUser) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.secondaryContainer
                }
            )
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                // Nombre del usuario (solo para mensajes de otros)
                if (!isCurrentUser) {
                    Text(
                        text = message.userName,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }

                // Contenido del mensaje
                Text(
                    text = message.message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Timestamp
                Text(
                    text = formatTimestamp(message.timestamp.toDate()),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.6f),
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

/**
 * Formatea el timestamp para mostrar solo la hora.
 */
private fun formatTimestamp(date: Date): String {
    val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    return formatter.format(date)
}

// ==================== PREVIEWS ====================

@Preview(name = "Mensaje propio", showSystemUi = true, showBackground = true)
@Composable
private fun MessageBubbleCurrentUserPreview() {
    MaterialTheme {
        Surface {
            Column(modifier = Modifier.padding(16.dp)) {
                MessageBubble(
                    message = GroupMessage(
                        id = "1",
                        userId = "currentUser",
                        userName = "Yo",
                        userPhotoUrl = "",
                        message = "Hola! Este es un mensaje que yo envié al grupo",
                        timestamp = Timestamp.now()
                    ),
                    isCurrentUser = true
                )
            }
        }
    }
}

@Preview(name = "Mensaje de otro usuario", showSystemUi = true, showBackground = true)
@Composable
private fun MessageBubbleOtherUserPreview() {
    MaterialTheme {
        Surface {
            Column(modifier = Modifier.padding(16.dp)) {
                MessageBubble(
                    message = GroupMessage(
                        id = "2",
                        userId = "otherUser",
                        userName = "Juan Pérez",
                        userPhotoUrl = "",
                        message = "Hola! Nos vemos en el plan, llego a las 7:00 PM",
                        timestamp = Timestamp.now()
                    ),
                    isCurrentUser = false
                )
            }
        }
    }
}

@Preview(name = "Mensaje largo", showSystemUi = true, showBackground = true)
@Composable
private fun MessageBubbleLongTextPreview() {
    MaterialTheme {
        Surface {
            Column(modifier = Modifier.padding(16.dp)) {
                MessageBubble(
                    message = GroupMessage(
                        id = "3",
                        userId = "otherUser",
                        userName = "María González",
                        userPhotoUrl = "",
                        message = "Este es un mensaje mucho más largo para ver cómo se comporta el componente cuando el texto ocupa múltiples líneas. Debería verse bien formateado y ser fácil de leer.",
                        timestamp = Timestamp.now()
                    ),
                    isCurrentUser = false
                )
            }
        }
    }
}
