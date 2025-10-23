package com.crewup.myapplication.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import com.crewup.myapplication.ui.components.InputWrapper
/**
 * Componente para seleccionar el rango de edad.
 *
 * @param modifier Modificador para el componente.
 * @param onAgeRangeChanged Callback con el rango de edad seleccionado.
 */
@Composable
fun AgeRangeSelector(
    modifier: Modifier = Modifier,
    onAgeRangeChanged: (Int, Int) -> Unit
) {
    var minAge by remember { mutableStateOf(18) }
    var maxAge by remember { mutableStateOf(65) }

    InputWrapper(
        title = "Rango de Edad",
        description = "Selecciona el rango de edad para tu plan.",
        detail = null,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = modifier
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
                value = maxAge.toFloat(),
                onValueChange = { newMaxAge ->
                    maxAge = newMaxAge.toInt().coerceIn(minAge + 1, 65)
                    onAgeRangeChanged(minAge, maxAge)
                },
                valueRange = 18f..65f,
                steps = 4,
                modifier = Modifier.weight(1f)
            )
            Text(text = "$minAge - $maxAge", fontSize = 14.sp)
        }
    }
}

/**
 * Componente para seleccionar el género.
 *
 * @param modifier Modificador para el componente.
 * @param onGenderSelected Callback con el género seleccionado.
 */
@Composable
fun GenderSelector(
    modifier: Modifier = Modifier,
    onGenderSelected: (String) -> Unit
) {
    var selectedGender by remember { mutableStateOf("Todos") }

    InputWrapper(
        title = "Género",
        description = "Elige el género preferido para tu plan.",
        detail = null,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { selectedGender = "M"; onGenderSelected("M") }) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_lock_power_off), // Placeholder for male icon
                    contentDescription = "Male Icon"
                )
                Text(text = "M", fontSize = 14.sp)
            }
            IconButton(onClick = { selectedGender = "F"; onGenderSelected("F") }) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_lock_power_off), // Placeholder for female icon
                    contentDescription = "Female Icon"
                )
                Text(text = "F", fontSize = 14.sp)
            }
            IconButton(onClick = { selectedGender = "Todos"; onGenderSelected("Todos") }) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_info_details), // Placeholder for all icon
                    contentDescription = "All Icon"
                )
                Text(text = "Todos", fontSize = 14.sp)
            }
        }
    }
}