package com.crewup.myapplication.ui.components.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.crewup.myapplication.R
import com.crewup.myapplication.models.GroupMessage
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch

/**
 * Componente que muestra la lista de mensajes del chat.
 * Separado para facilitar la personalización del diseño.
 *
 * @param messages Lista de mensajes a mostrar
 * @param currentUserId ID del usuario actual
 * @param onMessageLongPress Callback cuando se mantiene presionado un mensaje
 */
@Composable
fun MessagesList(
    messages: List<GroupMessage>,
    currentUserId: String?,
    onMessageLongPress: (GroupMessage) -> Unit = {}
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            coroutineScope.launch {
                listState.animateScrollToItem(messages.size - 1)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (messages.isEmpty()) {
            EmptyMessagesPlaceholder()
        } else {
            // CONTENEDOR CON BORDES REDONDEADOS
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp), // ESPACIO LATERAL
                shape = RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 20.dp,
                    bottomStart = 0.dp,
                    bottomEnd = 0.dp
                ),
                color = Color.White // Fondo blanco para que se vea el azul por los bordes
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        horizontal = 8.dp,  // Espacio interno
                        vertical = 12.dp
                    )
                ) {
                    items(
                        items = messages,
                        key = { it.id }
                    ) { message ->
                        MessageBubble(
                            message = message,
                            isCurrentUser = message.userId == currentUserId,
                            onLongPress = { onMessageLongPress(message) }
                        )
                    }
                }
            }
        }
    }
}
/**
 * Placeholder que se muestra cuando no hay mensajes.
 */
@Composable
private fun EmptyMessagesPlaceholder() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.chat_no_messages_title),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.chat_no_messages_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
        }
    }
}

// ==================== PREVIEWS ====================

@Preview(name = "Lista vacía", showSystemUi = true, showBackground = true)
@Composable
private fun MessagesListEmptyPreview() {
    MaterialTheme {
        Surface {
            MessagesList(
                messages = emptyList(),
                currentUserId = "currentUser"
            )
        }
    }
}

@Preview(name = "Lista con mensajes", showSystemUi = true, showBackground = true)
@Composable
private fun MessagesListWithMessagesPreview() {
    val mockMessages = listOf(
        GroupMessage(
            id = "1",
            userId = "otherUser1",
            userName = "Juan Pérez",
            userPhotoUrl = "",
            message = "Hola a todos! ¿Cómo están?",
            timestamp = Timestamp.now()
        ),
        GroupMessage(
            id = "2",
            userId = "currentUser",
            userName = "Yo",
            userPhotoUrl = "",
            message = "Hola Juan! Todo bien por aquí",
            timestamp = Timestamp.now()
        ),
        GroupMessage(
            id = "3",
            userId = "otherUser2",
            userName = "María González",
            userPhotoUrl = "",
            message = "¡Perfecto! Nos vemos en el plan",
            timestamp = Timestamp.now()
        ),
        GroupMessage(
            id = "4",
            userId = "currentUser",
            userName = "Yo",
            userPhotoUrl = "",
            message = "¡Claro! Yo llego a las 7:00 PM",
            timestamp = Timestamp.now()
        ),
        GroupMessage(
            id = "5",
            userId = "otherUser1",
            userName = "Juan Pérez",
            userPhotoUrl = "",
            message = "Yo llego un poco antes, los espero allá",
            timestamp = Timestamp.now()
        )
    )

    MaterialTheme {
        Surface {
            MessagesList(
                messages = mockMessages,
                currentUserId = "currentUser"
            )
        }
    }
}
