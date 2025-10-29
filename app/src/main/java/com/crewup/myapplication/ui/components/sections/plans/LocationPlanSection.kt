package com.crewup.myapplication.ui.components.sections.plans

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.crewup.myapplication.R
import com.crewup.myapplication.models.PlanLocation
import com.crewup.myapplication.ui.components.plans.PlaceSearch
import com.crewup.myapplication.viewmodel.LocationViewModel

@Composable
fun LocationPlanSection(
    initialQuery: String = "",
    onLocationSelected: (PlanLocation) -> Unit = {},
    viewModel: LocationViewModel? = null
) {
    val vm: LocationViewModel = viewModel ?: viewModel()
    val suggestions by vm.suggestions.collectAsState()
    val isLoading by vm.isLoading.collectAsState()
    val error by vm.error.collectAsState()
    var query by remember(initialQuery) { mutableStateOf(initialQuery) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Busca un lugar para tu plan", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        PlaceSearch(
            query = query,
            onQueryChange = { newQuery ->
                query = newQuery
                vm.searchPlaces(newQuery)
            },
            suggestions = suggestions,
            onSuggestionClick = { suggestion ->
                query = suggestion.description
                vm.clearSuggestions()
                // Convertir sugerencia a PlanLocation
                val location = PlanLocation(
                    id = suggestion.placeId,
                    name = suggestion.description.split(",").firstOrNull() ?: suggestion.description,
                    fullAddress = suggestion.description,
                    lat = 0.0, // Estos valores se actualizarían con Place Details API
                    lng = 0.0
                )
                onLocationSelected(location)
            },
            isLoading = isLoading,
            error = error
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LocationPlanSectionPreview() {
    LocationPlanSection(

    )
}
