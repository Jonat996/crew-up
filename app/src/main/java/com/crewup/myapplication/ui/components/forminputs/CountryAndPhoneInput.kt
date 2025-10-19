package com.crewup.myapplication.ui.components.forminputs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.crewup.myapplication.ui.components.Country

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryAndPhoneInput(
    countries: List<Country>,
    selectedCountry: Country,
    onCountrySelected: (Country) -> Unit,
    expandedCountry: Boolean,
    onExpandedCountryChange: (Boolean) -> Unit,
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ExposedDropdownMenuBox(
            expanded = expandedCountry,
            onExpandedChange = onExpandedCountryChange,
            modifier = Modifier
                .weight(0.45f)
                .height(56.dp)
        ) {
            OutlinedTextField(
                value = selectedCountry.code,
                onValueChange = {},
                readOnly = true,
                textStyle = LocalTextStyle.current.copy(fontSize = 15.sp),
                leadingIcon = {
                    AsyncImage(
                        model = selectedCountry.flagUrl,
                        contentDescription = selectedCountry.name,
                        modifier = Modifier
                            .size(22.dp)
                            .clip(RoundedCornerShape(3.dp))
                    )
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCountry)
                },
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(5.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedBorderColor = Color.LightGray,
                    focusedBorderColor = MaterialTheme.colorScheme.primary
                )
            )

            ExposedDropdownMenu(
                expanded = expandedCountry,
                onDismissRequest = { onExpandedCountryChange(false) }
            ) {
                countries.forEach { country ->
                    DropdownMenuItem(
                        text = { Text("${country.name} (${country.code})") },
                        onClick = {
                            onCountrySelected(country)
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = {
                if (it.all { char -> char.isDigit() }) {
                    onPhoneNumberChange(it)
                }
            },
            label = { Text("Número") },
            singleLine = true,
            modifier = Modifier
                .weight(0.65f)
                .height(60.dp),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = MaterialTheme.colorScheme.primary
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = LocalTextStyle.current.copy(
                fontSize = 17.sp,
                lineHeight = 22.sp
            )
        )
    }
}
