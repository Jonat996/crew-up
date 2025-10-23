package com.crewup.myapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Componente para agregar y mostrar etiquetas personalizadas.
 *
 * @param selectedTags Lista de tags actualmente seleccionados.
 * @param onTagsChanged Callback cuando cambia la lista de tags.
 * @param modifier Modificador para el componente.
 */
@Composable
fun CategorySelector(
    selectedTags: List<String> = emptyList(),
    onTagsChanged: (List<String>) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var inputText by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(modifier = modifier.fillMaxWidth()) {
        // Campo de texto para agregar nuevas etiquetas
        OutlinedTextField(
            value = inputText,
            onValueChange = { inputText = it },
            placeholder = { Text("Escribe una etiqueta y presiona Enter") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF0056B3),
                unfocusedBorderColor = Color(0xFF165BB0),
                focusedLabelColor = Color(0xFF0056B3),
                unfocusedLabelColor = Color(0xFF165BB0)
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (inputText.isNotBlank() && !selectedTags.contains(inputText.trim())) {
                        onTagsChanged(selectedTags + inputText.trim())
                        inputText = ""
                    }
                    keyboardController?.hide()
                }
            ),
            singleLine = true
        )

        // Mostrar tags seleccionados con FlowRow en lugar de LazyRow
        if (selectedTags.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                selectedTags.forEach { tag ->
                    TagChip(
                        tag = tag,
                        onRemove = {
                            onTagsChanged(selectedTags - tag)
                        }
                    )
                }
            }
        } else {
            // Mensaje cuando no hay tags
            Text(
                text = "No hay etiquetas agregadas",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
    }
}

/**
 * Componente de chip para representar una etiqueta con botón de eliminar.
 *
 * @param tag Texto de la etiqueta.
 * @param onRemove Callback cuando se presiona el botón X.
 * @param modifier Modificador para el chip.
 */
@Composable
fun TagChip(
    tag: String,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFF0056B3),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(start = 12.dp, end = 4.dp, top = 6.dp, bottom = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = tag,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            // Botón X para eliminar
            IconButton(
                onClick = onRemove,
                modifier = Modifier
                    .size(20.dp)
                    .background(Color.White.copy(alpha = 0.3f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Eliminar etiqueta",
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategorySelectorPreview() {
    var tags by remember { mutableStateOf(listOf("Juegos de mesa", "Cantar", "Deportes")) }

    CategorySelector(
        selectedTags = tags,
        onTagsChanged = { tags = it },
        modifier = Modifier.padding(16.dp)
    )
}