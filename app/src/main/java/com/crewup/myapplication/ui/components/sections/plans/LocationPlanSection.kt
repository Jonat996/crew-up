package com.crewup.myapplication.ui.components.sections.plans

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
    userCountry: String? = null,
    userCity: String? = null,
    onLocationSelected: (PlanLocation) -> Unit = {},
    viewModel: LocationViewModel? = null
) {
    val vm: LocationViewModel = viewModel ?: viewModel()
    val suggestions by vm.suggestions.collectAsState()
    val isLoading by vm.isLoading.collectAsState()
    val error by vm.error.collectAsState()
    var query by remember(initialQuery) { mutableStateOf(initialQuery) }

    // Configurar la ubicación del usuario en el ViewModel para filtrar búsquedas
    LaunchedEffect(userCountry, userCity) {
        vm.setUserLocation(userCountry, userCity)
    }

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

                // Extraer el nombre del lugar y la dirección completa
                // Ejemplo: "Parque El Virrey, Calle 88 #9-99, Bogotá"
                // name = "Parque El Virrey"
                // fullAddress = "Parque El Virrey, Calle 88 #9-99, Bogotá"
                val parts = suggestion.description.split(",").map { it.trim() }
                val locationName = when {
                    parts.size >= 2 -> parts[0] // Primera parte es el nombre del lugar
                    else -> suggestion.description
                }

                // Convertir sugerencia a PlanLocation
                val location = PlanLocation(
                    id = suggestion.placeId,
                    name = locationName,
                    fullAddress = suggestion.description,
                    lat = 0.0, // TODO: Obtener coordenadas con Place Details API
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
