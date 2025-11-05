package com.crewup.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.crewup.myapplication.R
import com.crewup.myapplication.ui.components.chat.ChatHeader
import com.crewup.myapplication.ui.components.chat.MessagesList
import com.crewup.myapplication.viewmodel.GroupChatViewModel

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

    LaunchedEffect(planId) { viewModel.initializeChat(planId) }

    error?.let { errorMessage ->
        LaunchedEffect(errorMessage) {
            android.util.Log.e("GroupChatScreen", errorMessage)
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            ChatHeader(
                planTitle = plan?.title ?: stringResource(R.string.chat_group_title),
                participantCount = (plan?.participants?.size ?: 0) + 1,
                participants = plan?.participants.orEmpty(),
                onBackClick = onNavigateBack
            )
        },
        containerColor = Color(0xFF165BB0), // FONDO AZUL GLOBAL
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (plan == null) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                // TODO DENTRO DE MessagesList
                MessagesList(
                    messages = messages,
                    currentUserId = currentUserId,
                    messageText = messageText,
                    onMessageTextChange = { viewModel.updateMessageText(it) },
                    onSendClick = { viewModel.sendMessage() },
                    onMessageLongPress = { message ->
                        if (message.userId == currentUserId) {
                            viewModel.deleteMessage(message)
                        }
                    },
                    enabled = !isLoading
                )
            }

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