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
import com.crewup.myapplication.ui.components.plans.PlaceSearch
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

                query = suggestion.description
                viewModel.clearSuggestions()
            },
            isLoading = isLoading,
            error = error
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {  },
                modifier = Modifier
                    .widthIn(min = 140.dp, max = 200.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0056B3),
                    contentColor = Color.White,
                ),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                Text(
                    text = stringResource(R.string.button_plan),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LocationPlanSectionPreview() {
    LocationPlanSection(

    )
}
