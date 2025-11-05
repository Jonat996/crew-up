package com.crewup.myapplication.ui.components.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        shape = RoundedCornerShape(28.dp),
        color = Color.White
        // SIN sombra externa → no flota visualmente
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .shadow(4.dp, RoundedCornerShape(24.dp)) // SOMBRA INTERNA
                .background(Color.White)
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Emoji
            IconButton(
                onClick = { /* TODO: Emoji picker */ },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Mood,
                    contentDescription = "Emoji",
                    tint = Color(0xFF666666)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Campo de texto
            BasicTextField(
                value = messageText,
                onValueChange = onMessageTextChange,
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 40.dp),
                enabled = enabled,
                textStyle = LocalTextStyle.current.copy(
                    color = Color.Black,
                    fontSize = 16.sp
                ),
                decorationBox = { innerTextField ->
                    if (messageText.isEmpty()) {
                        Text(
                            text = stringResource(R.string.chat_input_placeholder),
                            color = Color(0xFF999999),
                            fontSize = 16.sp
                        )
                    }
                    innerTextField()
                }
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Botón enviar (avión)
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
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Enviar",
                    tint = Color.White
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
