package com.crewup.myapplication.ui.components.forminputs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.crewup.myapplication.ui.components.CountryStructure

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityInput(
    selectedCity: String,
    onCitySelected: (String) -> Unit,
    expandedCity: Boolean,
    onExpandedCityChange: (Boolean) -> Unit,
    selectedCountry: CountryStructure,
    onCountrySelected: (CountryStructure) -> Unit,
    countries: List<CountryStructure>,
    modifier: Modifier = Modifier
) {
    ExposedDropdownMenuBox(
        expanded = expandedCity,
        onExpandedChange = onExpandedCityChange,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        OutlinedTextField(
            value = selectedCity,
            onValueChange = {},
            readOnly = true,
            label = { Text("Ciudad") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCity) },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true)
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = MaterialTheme.colorScheme.primary
            )
        )

        ExposedDropdownMenu(
            expanded = expandedCity,
            onDismissRequest = { onExpandedCityChange(false) }
        ) {
            // 🔹 Primero las ciudades del país seleccionado
            selectedCountry.cities.forEach { city ->
                DropdownMenuItem(
                    text = { Text(city) },
                    onClick = {
                        onCitySelected(city)
                        onExpandedCityChange(false)
                    }
                )
            }

            // 🔹 Separador visual (opcional)
            Divider()

            // 🔹 Luego las ciudades de los demás países
            countries
                .filter { it != selectedCountry }
                .forEach { country ->
                    country.cities.forEach { city ->
                        DropdownMenuItem(
                            text = { Text("$city (${country.name})") },
                            onClick = {
                                onCitySelected(city)
                                onCountrySelected(country)
                                onExpandedCityChange(false)
                            }
                        )
                    }
                }
        }
    }
}
