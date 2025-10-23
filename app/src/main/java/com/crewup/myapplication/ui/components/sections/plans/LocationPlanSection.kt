package com.crewup.myapplication.ui.components.sections.plans

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import com.crewup.myapplication.ui.components.plans.PlaceSearch
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.crewup.myapplication.viewmodel.LocationViewModel

@Composable
fun LocationPlanSection() {
    val viewModel: LocationViewModel = viewModel()
    val suggestions by viewModel.suggestions.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    var query by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Busca un lugar para tu plan", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        PlaceSearch(
            query = query,
            onQueryChange = { newQuery ->
                query = newQuery
                viewModel.searchPlaces(newQuery)
            },
            suggestions = suggestions,
            onSuggestionClick = { suggestion ->
                // Aquí maneja la selección (e.g., guarda en un plan o navega)
                query = suggestion.description // Opcional: Actualiza el campo con la selección
                viewModel.clearSuggestions()
            },
            isLoading = isLoading,
            error = error
        )
    }
}