package com.crewup.myapplication.ui.components.sections.plans

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.tooling.preview.Preview
import com.crewup.myapplication.ui.components.CategorySelector
import com.crewup.myapplication.ui.components.InputWrapper

/**
 * Sección para configurar categorías y participantes del plan.
 *
 * @param modifier Modificador para la sección.
 * @param selectedTags Tags actualmente seleccionados.
 * @param minAge Edad mínima actual.
 * @param maxAge Edad máxima actual.
 * @param selectedGender Género seleccionado actual.
 * @param onTagsChange Callback cuando cambian los tags.
 * @param onAgeRangeChanged Callback cuando cambia el rango de edad.
 * @param onGenderChanged Callback cuando cambia el género.
 */
@Composable
fun PlanConfigurationSection(
    modifier: Modifier = Modifier,
    selectedTags: List<String> = emptyList(),
    minAge: Int = 18,
    maxAge: Int = 65,
    selectedGender: String = "Todos",
    onTagsChange: (List<String>) -> Unit = {},
    onAgeRangeChanged: (Int, Int) -> Unit = { _, _ -> },
    onGenderChanged: (String) -> Unit = {}
) {
    var selectedCategories by remember(selectedTags) { mutableStateOf(selectedTags) }
    var currentMinAge by remember(minAge) { mutableStateOf(minAge) }
    var currentMaxAge by remember(maxAge) { mutableStateOf(maxAge) }
    var currentGender by remember(selectedGender) { mutableStateOf(selectedGender) }

    Column(modifier = modifier) {

        // InputWrapper para categorizar el plan
        InputWrapper(
            title = "Categoriza tu plan",
            description = "Pon etiquetas para armar el Crew perfecto y disfrutar de tu plan.",
            detail = "Asegúrate que las etiquetas sean acorde al plan.",
            modifier = Modifier.fillMaxWidth()
        ) {
            CategorySelector(
                selectedTags = selectedCategories,
                onTagsChanged = { newTags ->
                    selectedCategories = newTags
                    onTagsChange(newTags)
                }
            )
        }

        // InputWrapper para configurar quiénes pueden unirse
        InputWrapper(
            title = "¿Quiénes pueden unirse?",
            description = "Asegúrate que tener la gente adecuada para tu plan",
            detail = null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Age Range Selector
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_menu_gallery), // Placeholder, replace with age icon
                        contentDescription = "Age Icon",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Slider(
                        value = currentMaxAge.toFloat(),
                        onValueChange = { newMaxAge ->
                            currentMaxAge = newMaxAge.toInt().coerceIn(currentMinAge + 1, 65)
                            onAgeRangeChanged(currentMinAge, currentMaxAge)
                        },
                        valueRange = 18f..65f,
                        steps = 4,
                        modifier = Modifier.weight(1f)
                    )
                    Text(text = "$currentMinAge - $currentMaxAge", fontSize = 14.sp)
                }

                // Gender Selector
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        currentGender = "M"
                        onGenderChanged("M")
                    }) {
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_lock_power_off), // Placeholder for male icon
                            contentDescription = "Male Icon"
                        )
                        Text(text = "M", fontSize = 14.sp)
                    }
                    IconButton(onClick = {
                        currentGender = "F"
                        onGenderChanged("F")
                    }) {
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_lock_power_off), // Placeholder for female icon
                            contentDescription = "Female Icon"
                        )
                        Text(text = "F", fontSize = 14.sp)
                    }
                    IconButton(onClick = {
                        currentGender = "Todos"
                        onGenderChanged("Todos")
                    }) {
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_menu_info_details), // Placeholder for all icon
                            contentDescription = "All Icon"
                        )
                        Text(text = "Todos", fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewPlanConfigurationSection() {
    PlanConfigurationSection(
        modifier = Modifier
    )
}