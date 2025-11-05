package com.crewup.myapplication.ui.components.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crewup.myapplication.R
import com.crewup.myapplication.models.GroupMessage
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch

/**
 * Componente principal del chat: lista + input + estado vacío
 */
@Composable
fun MessagesList(
    messages: List<GroupMessage>,
    currentUserId: String?,
    messageText: String,
    onMessageTextChange: (String) -> Unit,
    onSendClick: () -> Unit,
    onMessageLongPress: (GroupMessage) -> Unit = {},
    enabled: Boolean = true
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

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        color = Color.White
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // --- Área de mensajes o placeholder ---
            if (messages.isEmpty()) {
                EmptyChatPlaceholder(
                    modifier = Modifier
                        .weight(1f)  // AQUÍ SÍ: dentro de Column
                        .fillMaxWidth()
                )
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 12.dp)
                ) {
                    items(messages, key = { it.id }) { message ->
                        MessageBubble(
                            message = message,
                            isCurrentUser = message.userId == currentUserId,
                            onLongPress = { onMessageLongPress(message) }
                        )
                    }
                }
            }

            // --- Input fijo ---
            MessageInputRow(
                messageText = messageText,
                onMessageTextChange = onMessageTextChange,
                onSendClick = onSendClick,
                enabled = enabled
            )
        }
    }
}

/**
 * Placeholder cuando no hay mensajes
 */
@Composable
private fun EmptyChatPlaceholder(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier, // Aquí pasamos el weight
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.ChatBubbleOutline,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = Color(0xFF165BB0).copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Aún no hay mensajes",
                color = Color(0xFF666666),
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = "¡Sé el primero en escribir!",
                color = Color(0xFF999999),
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Input con borde azul, sombra y redondeo
 */
@Composable
private fun MessageInputRow(
    messageText: String,
    onMessageTextChange: (String) -> Unit,
    onSendClick: () -> Unit,
    enabled: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { /* TODO: Emoji picker */ }, modifier = Modifier.size(40.dp)) {
            Icon(Icons.Default.Mood, contentDescription = "Emoji", tint = Color(0xFF666666))
        }

        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .heightIn(min = 44.dp)
                .shadow(elevation = 4.dp, shape = RoundedCornerShape(22.dp))
                .background(Color.White, RoundedCornerShape(22.dp))
                .border(1.5.dp, Color(0xFF165BB0), RoundedCornerShape(22.dp))
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            BasicTextField(
                value = messageText,
                onValueChange = onMessageTextChange,
                modifier = Modifier.fillMaxWidth(),
                textStyle = LocalTextStyle.current.copy(color = Color.Black, fontSize = 16.sp),
                decorationBox = { innerTextField ->
                    if (messageText.isEmpty()) {
                        Text(
                            "Escribe un mensaje...",
                            color = Color(0xFF999999),
                            fontSize = 16.sp,
                            modifier = Modifier.align(Alignment.CenterStart)
                        )
                    }
                    innerTextField()
                }
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(
            onClick = onSendClick,
            enabled = enabled && messageText.isNotBlank(),
            modifier = Modifier
                .size(44.dp)
                .background(
                    color = if (enabled && messageText.isNotBlank()) Color(0xFF165BB0) else Color(0xFFCCCCCC),
                    shape = CircleShape
                )
        ) {
            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Enviar", tint = Color.White)
        }
    }
}

// ==================== PREVIEWS ====================

@Preview(name = "Sin mensajes - Estado vacío", showSystemUi = true, showBackground = true)
@Composable
private fun MessagesListEmptyPreview() {
    var messageText by remember { mutableStateOf("") }
    MaterialTheme {
        Surface(color = Color(0xFF165BB0)) {
            MessagesList(
                messages = emptyList(),
                currentUserId = "currentUser",
                messageText = messageText,
                onMessageTextChange = { messageText = it },
                onSendClick = { },
                enabled = true
            )
        }
    }
}

@Preview(name = "Con mensajes", showSystemUi = true, showBackground = true)
@Composable
private fun MessagesListWithMessagesPreview() {
    var messageText by remember { mutableStateOf("") }
    val mockMessages = listOf(
        GroupMessage("1", "otherUser1", "Juan Pérez", "", "Hola a todos! ¿Cómo están?", Timestamp.now()),
        GroupMessage("2", "currentUser", "Yo", "", "Hola Juan! Todo bien por aquí", Timestamp.now()),
        GroupMessage("3", "otherUser2", "María González", "", "¡Perfecto! Nos vemos en el plan", Timestamp.now()),
        GroupMessage("4", "currentUser", "Yo", "", "¡Claro! Yo llego a las 7:00 PM", Timestamp.now()),
        GroupMessage("5", "otherUser1", "Juan Pérez", "", "Yo llego un poco antes, los espero allá", Timestamp.now())
    )
    MaterialTheme {
        Surface(color = Color(0xFF165BB0)) {
            MessagesList(
                messages = mockMessages,
                currentUserId = "currentUser",
                messageText = messageText,
                onMessageTextChange = { messageText = it },
                onSendClick = { },
                enabled = true
            )
        }
    }
}