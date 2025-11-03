package com.crewup.myapplication.ui.components.chat

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.crewup.myapplication.R

/**
 * Componente de encabezado del chat.
 * Separado para facilitar la personalización del diseño.
 *
 * @param planTitle Título del plan
 * @param participantCount Cantidad de participantes
 * @param onBackClick Callback cuando se presiona el botón de regresar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatHeader(
    planTitle: String,
    participantCount: Int,
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = planTitle,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = stringResource(R.string.chat_participants, participantCount),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.chat_back)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

// ==================== PREVIEWS ====================

@Preview(name = "Header con pocos participantes", showSystemUi = true, showBackground = true)
@Composable
private fun ChatHeaderPreview() {
    MaterialTheme {
        Surface {
            ChatHeader(
                planTitle = "Intercambio de idiomas",
                participantCount = 5,
                onBackClick = { }
            )
        }
    }
}

@Preview(name = "Header con muchos participantes", showSystemUi = true, showBackground = true)
@Composable
private fun ChatHeaderManyParticipantsPreview() {
    MaterialTheme {
        Surface {
            ChatHeader(
                planTitle = "Partido de fútbol en el parque",
                participantCount = 24,
                onBackClick = { }
            )
        }
    }
}

@Preview(name = "Header con título largo", showSystemUi = true, showBackground = true)
@Composable
private fun ChatHeaderLongTitlePreview() {
    MaterialTheme {
        Surface {
            ChatHeader(
                planTitle = "Reunión de estudio para preparar el examen final de matemáticas avanzadas",
                participantCount = 12,
                onBackClick = { }
            )
        }
    }
}
