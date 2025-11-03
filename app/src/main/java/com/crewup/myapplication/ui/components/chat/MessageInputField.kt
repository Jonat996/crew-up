package com.crewup.myapplication.ui.components.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.crewup.myapplication.R

/**
 * Componente de campo de entrada de mensajes.
 * Separado para facilitar la personalización del diseño.
 *
 * @param messageText Texto del mensaje actual
 * @param onMessageTextChange Callback cuando cambia el texto
 * @param onSendClick Callback cuando se presiona enviar
 * @param enabled Si el campo está habilitado
 */
@Composable
fun MessageInputField(
    messageText: String,
    onMessageTextChange: (String) -> Unit,
    onSendClick: () -> Unit,
    enabled: Boolean = true
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            // Campo de texto
            OutlinedTextField(
                value = messageText,
                onValueChange = onMessageTextChange,
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 56.dp, max = 120.dp),
                placeholder = { Text(stringResource(R.string.chat_input_placeholder)) },
                enabled = enabled,
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                ),
                maxLines = 4
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Botón de enviar
            FilledIconButton(
                onClick = onSendClick,
                modifier = Modifier.size(56.dp),
                enabled = enabled && messageText.isNotBlank(),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = stringResource(R.string.chat_send_message)
                )
            }
        }
    }
}

// ==================== PREVIEWS ====================

@Preview(name = "Campo vacío", showSystemUi = true, showBackground = true)
@Composable
private fun MessageInputFieldEmptyPreview() {
    var text by remember { mutableStateOf("") }

    MaterialTheme {
        Surface {
            MessageInputField(
                messageText = text,
                onMessageTextChange = { text = it },
                onSendClick = { }
            )
        }
    }
}

@Preview(name = "Campo con texto", showSystemUi = true, showBackground = true)
@Composable
private fun MessageInputFieldWithTextPreview() {
    var text by remember { mutableStateOf("Hola! Este es un mensaje de prueba") }

    MaterialTheme {
        Surface {
            MessageInputField(
                messageText = text,
                onMessageTextChange = { text = it },
                onSendClick = { }
            )
        }
    }
}

@Preview(name = "Campo deshabilitado", showSystemUi = true, showBackground = true)
@Composable
private fun MessageInputFieldDisabledPreview() {
    var text by remember { mutableStateOf("") }

    MaterialTheme {
        Surface {
            MessageInputField(
                messageText = text,
                onMessageTextChange = { text = it },
                onSendClick = { },
                enabled = false
            )
        }
    }
}

@Preview(name = "Campo con texto largo", showSystemUi = true, showBackground = true)
@Composable
private fun MessageInputFieldLongTextPreview() {
    var text by remember { mutableStateOf("Este es un mensaje muy largo que ocupa múltiples líneas para ver cómo se comporta el campo de entrada cuando hay mucho texto.") }

    MaterialTheme {
        Surface {
            MessageInputField(
                messageText = text,
                onMessageTextChange = { text = it },
                onSendClick = { }
            )
        }
    }
}
