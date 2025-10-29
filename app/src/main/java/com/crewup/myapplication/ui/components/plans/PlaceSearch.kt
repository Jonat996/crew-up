package com.crewup.myapplication.ui.components.plans

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.crewup.myapplication.R
import com.crewup.myapplication.models.PlaceSuggestion


@Composable
fun PlaceSearch(
    query: String,
    onQueryChange: (String) -> Unit,
    suggestions: List<PlaceSuggestion>,
    onSuggestionClick: (PlaceSuggestion) -> Unit,
    isLoading: Boolean,
    error: String?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .shadow(8.dp, shape = RoundedCornerShape(10.dp))
            .zIndex(1f),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                label = { Text("Ingresa una ubicación") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_menu_search), // Cambia por tu icono de lugar
                        contentDescription = "Search Icon"
                    )
                },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_menu_close_clear_cancel),
                            contentDescription = "Clear",
                            modifier = Modifier.clickable { onQueryChange("") }
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            error?.let {
                Text("Error: $it", color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 12.dp))
            }

            // Usar Column simple en lugar de LazyColumn para evitar problemas de scroll anidado
            if (suggestions.isNotEmpty()) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    suggestions.forEach { suggestion ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSuggestionClick(suggestion) }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.vector),
                                contentDescription = "Location Icon",
                                tint = Color(0xFF0056B3),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(text = suggestion.description, style = MaterialTheme.typography.bodyLarge)
                                suggestion.secondaryText?.let {
                                    Text(text = it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                                }
                            }
                        }
                        if (suggestion != suggestions.last()) {
                            Divider()
                        }
                    }
                }
            }
        }
    }
}


@Preview (showBackground = true, showSystemUi = true)
@Composable
fun PreviewPlaceSearch() {
    val sampleSuggestions = listOf(
        PlaceSuggestion(placeId = "1", description = "Eiffel Tower", secondaryText = "Paris, France"),
        PlaceSuggestion(placeId = "2", description = "Statue of Liberty", secondaryText = "New York, USA"),
        PlaceSuggestion(placeId = "3", description = "Colosseum", secondaryText = "Rome, Italy")
    )
    PlaceSearch(
        query = "String",
        onQueryChange = {  },
        suggestions = sampleSuggestions,
        onSuggestionClick = {  },
        isLoading = false,
        error = null
    )
}