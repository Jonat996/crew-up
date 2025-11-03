package com.crewup.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.crewup.myapplication.R
import com.crewup.myapplication.models.GroupMessage
import com.crewup.myapplication.ui.components.chat.ChatHeader
import com.crewup.myapplication.ui.components.chat.MessageInputField
import com.crewup.myapplication.ui.components.chat.MessagesList
import com.crewup.myapplication.viewmodel.GroupChatViewModel
import com.google.firebase.Timestamp

/**
 * Pantalla principal del chat grupal de un plan.
 *
 * @param planId ID del plan
 * @param onNavigateBack Callback para navegar hacia atrás
 * @param viewModel ViewModel del chat grupal
 */
@Composable
fun GroupChatScreen(
    planId: String,
    onNavigateBack: () -> Unit,
    viewModel: GroupChatViewModel = viewModel()
) {
    val plan by viewModel.plan.collectAsState()
    val messages by viewModel.messages.collectAsState()
    val currentUserId by viewModel.currentUserId.collectAsState()
    val messageText by viewModel.messageText.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    // Inicializar el chat cuando se carga la pantalla
    LaunchedEffect(planId) {
        viewModel.initializeChat(planId)
    }

    // Mostrar errores si existen
    error?.let { errorMessage ->
        LaunchedEffect(errorMessage) {
            // Aquí puedes mostrar un Snackbar o Toast
            android.util.Log.e("GroupChatScreen", errorMessage)
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            ChatHeader(
                planTitle = plan?.title ?: stringResource(R.string.chat_group_title),
                participantCount = plan?.participants?.size ?: 0,
                onBackClick = onNavigateBack
            )
        },
        bottomBar = {
            MessageInputField(
                messageText = messageText,
                onMessageTextChange = { viewModel.updateMessageText(it) },
                onSendClick = { viewModel.sendMessage() },
                enabled = !isLoading
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (plan == null) {
                // Mostrar loading mientras carga el plan
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                // Mostrar la lista de mensajes
                MessagesList(
                    messages = messages,
                    currentUserId = currentUserId,
                    onMessageLongPress = { message ->
                        // Implementar diálogo de confirmación para eliminar
                        if (message.userId == currentUserId) {
                            viewModel.deleteMessage(message)
                        }
                    }
                )
            }

            // Mostrar loading indicator cuando se está enviando un mensaje
            if (isLoading && plan != null) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                )
            }
        }
    }
}

// ==================== PREVIEWS ====================

@Preview(name = "Chat con mensajes", showSystemUi = true, showBackground = true)
@Composable
private fun GroupChatScreenPreview() {
    val mockMessages = listOf(
        GroupMessage(
            id = "1",
            userId = "user1",
            userName = "Juan Pérez",
            userPhotoUrl = "",
            message = "Hola a todos! ¿Listos para el plan?",
            timestamp = Timestamp.now()
        ),
        GroupMessage(
            id = "2",
            userId = "currentUser",
            userName = "Yo",
            userPhotoUrl = "",
            message = "Sí! Ya estoy listo, llego en 10 minutos",
            timestamp = Timestamp.now()
        ),
        GroupMessage(
            id = "3",
            userId = "user2",
            userName = "María González",
            userPhotoUrl = "",
            message = "Perfecto! Los espero en la entrada",
            timestamp = Timestamp.now()
        )
    )

    MaterialTheme {
        Scaffold(
            topBar = {
                ChatHeader(
                    planTitle = "Intercambio de idiomas",
                    participantCount = 8,
                    onBackClick = { }
                )
            },
            bottomBar = {
                MessageInputField(
                    messageText = "",
                    onMessageTextChange = { },
                    onSendClick = { }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                MessagesList(
                    messages = mockMessages,
                    currentUserId = "currentUser"
                )
            }
        }
    }
}

@Preview(name = "Chat vacío", showSystemUi = true, showBackground = true)
@Composable
private fun GroupChatScreenEmptyPreview() {
    MaterialTheme {
        Scaffold(
            topBar = {
                ChatHeader(
                    planTitle = "Partido de fútbol",
                    participantCount = 12,
                    onBackClick = { }
                )
            },
            bottomBar = {
                MessageInputField(
                    messageText = "",
                    onMessageTextChange = { },
                    onSendClick = { }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                MessagesList(
                    messages = emptyList(),
                    currentUserId = "currentUser"
                )
            }
        }
    }
}
