package com.crewup.myapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Componente para seleccionar categorías de un plan.
 *
 * @param onCategorySelected Callback cuando se selecciona una categoría.
 * @param modifier Modificador para el componente.
 */
@Composable
fun CategorySelector(
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf(TextFieldValue()) }
    val categories = listOf("Juegos de mesa", "Cantar", "Intercambio de idiomas", "Deportes", "Cine", "Comida")

    // Campo de búsqueda
    TextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        placeholder = { Text("Busca una categoría") },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        trailingIcon = {
            Icon(
                painter = painterResource(id = android.R.drawable.ic_menu_search),
                contentDescription = "Search Icon"
            )
        }
    )

    // Lista de categorías
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 30.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        categories.forEach { category ->
            CategoryChip(
                category = category,
                onClick = { onCategorySelected(category) },
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}

/**
 * Componente de chip para representar una categoría.
 *
 * @param category Nombre de la categoría.
 * @param onClick Callback cuando se hace clic en el chip.
 * @param modifier Modificador para el chip.
 */
@Composable
fun CategoryChip(
    category: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(Color(0xFF165BB0), RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = category,
            color = Color.White,
            fontSize = 14.sp
        )
    }
}

@Preview
@Composable
fun CategorySelectorPreview(){
    CategorySelector(
        onCategorySelected = {},
        modifier = Modifier
    )
}