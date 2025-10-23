package com.crewup.myapplication.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Componente envoltorio para inputs.
 * Muestra una Card con título en azul, descripción, detalle opcional en gris,
 * y un slot para el contenido del input.
 *
 * @param title Título principal en azul.
 * @param description Descripción principal.
 * @param detail Detalle opcional en gris (null para no mostrarlo).
 * @param modifier Modificador para la Card.
 * @param content Slot composable para el input o contenido personalizado.
 */
@Composable
fun InputWrapper(
    title: String,
    description: String,
    detail: String? = null,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = MaterialTheme.shapes.medium, // Rounded corners por defecto
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White // Fondo blanco para coincidir con el estilo de las pantallas
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            // Título en azul
            Text(
                text = title,
                color = Color(0xFF165BB0), // Color azul del tema (basado en drawables y colors.xml)
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.padding(4.dp))

            // Descripción
            Text(
                text = description,
                color = Color.Black,
                fontSize = 14.sp
            )

            // Detalle opcional en gris
            if (detail != null) {
                Spacer(modifier = Modifier.padding(4.dp))
                Text(
                    text = detail,
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.padding(8.dp))

            // Slot para el input o contenido
            content()
        }
    }
}

@Preview
@Composable
fun InputWrapperPreview() {
    InputWrapper(
        title = "Título",
        description = "Descripción principal",
        detail = "Detalle opcional"
    ) {

    }
}
