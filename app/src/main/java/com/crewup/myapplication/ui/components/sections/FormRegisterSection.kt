package com.crewup.myapplication.ui.components.sections

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.crewup.myapplication.viewmodel.AuthState

// ----------------------
// MODELO DE DATOS
// ----------------------
data class Country(
    val name: String,
    val code: String,
    val flagUrl: String,
    val cities: List<String>
)

val countries = listOf(
    Country(
        name = "Colombia",
        code = "+57",
        flagUrl = "https://flagcdn.com/w320/co.png",
        cities = listOf("Bogotá D.C.", "Medellín", "Cali", "Barranquilla")
    ),
    Country(
        name = "México",
        code = "+52",
        flagUrl = "https://flagcdn.com/w320/mx.png",
        cities = listOf("Ciudad de México", "Guadalajara", "Monterrey")
    ),
    Country(
        name = "Argentina",
        code = "+54",
        flagUrl = "https://flagcdn.com/w320/ar.png",
        cities = listOf("Buenos Aires", "Córdoba", "Rosario")
    ),
    Country(name = "Peru",
    code = "+12",
    flagUrl = "https://flagcdn.com/w320/ar.png",
    cities = listOf("lima")
)
)

// ----------------------
// FORMULARIO
// ----------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormRegister(
    authState: AuthState,
    isRegistering: Boolean,
    onEmailRegister: (name: String, lastName: String, email: String, password: String, country: String, city: String, phone: String) -> Unit,
    onGoogleLogin: () -> Unit,
    onClearError: () -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    // Estados nuevos para país, número y ciudad
    var selectedCountry by remember { mutableStateOf(countries[0]) }
    var expandedCountry by remember { mutableStateOf(false) }
    var phoneNumber by remember { mutableStateOf("") }

    var selectedCity by remember { mutableStateOf(selectedCountry.cities.first()) }
    var expandedCity by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .shadow(8.dp, shape = RoundedCornerShape(10.dp))
            .zIndex(1f),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = modifier.padding(horizontal = 14.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            // Campo nombre y apellido
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp)

                )
                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Apellido") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp)
                )
            }

            // Campo correo
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    if (authState.error != null) onClearError()
                },
                label = { Text("Ingresa tu correo") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                shape = RoundedCornerShape(10.dp),
            )

            // Campo contraseña
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    if (authState.error != null) onClearError()
                },
                label = { Text("Ingresa tu contraseña") },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon = if (showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                    Icon(
                        imageVector = icon,
                        contentDescription = "Mostrar contraseña",
                        modifier = Modifier.clickable { showPassword = !showPassword }
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                shape = RoundedCornerShape(10.dp)
            )

            // Campo confirmar contraseña
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmar tu contraseña") },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon = if (showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                    Icon(
                        imageVector = icon,
                        contentDescription = "Mostrar contraseña",
                        modifier = Modifier.clickable { showPassword = !showPassword }
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                shape = RoundedCornerShape(10.dp)
            )


            // CAMPOS NUEVOS: PAÍS / TELÉFONO / CIUDAD

            Spacer(Modifier.height(8.dp))



            // Selector de país y número
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Selector de país (indicativo)
                ExposedDropdownMenuBox(
                    expanded = expandedCountry,
                    onExpandedChange = { expandedCountry = !expandedCountry },
                    modifier = Modifier
                        .weight(0.45f) // más espacio para mostrar bandera y código
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
                        onDismissRequest = { expandedCountry = false }
                    ) {
                        countries.forEach { country ->
                            DropdownMenuItem(
                                text = { Text("${country.name} (${country.code})") },
                                onClick = {
                                    selectedCountry = country
                                    selectedCity = country.cities.first()
                                    expandedCountry = false
                                }
                            )
                        }
                    }
                }

                // Campo de número
                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { input ->
                        // 🔹 Solo permite dígitos numéricos
                        if (input.all { it.isDigit() }) {
                            phoneNumber = input
                        }
                    },
                    label = { Text("Número") },
                    singleLine = true,
                    modifier = Modifier
                        .weight(0.65f)
                        .height(60.dp), // 🔹 un poco más alto para dar aire
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedBorderColor = Color.LightGray,
                        focusedBorderColor = MaterialTheme.colorScheme.primary
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), // 🔹 solo números
                    textStyle = LocalTextStyle.current.copy(
                        fontSize = 17.sp, // 🔹 tamaño equilibrado
                        lineHeight = 22.sp // 🔹 más espacio interno vertical
                    )
                )
            }


            // Campo de ciudad
            ExposedDropdownMenuBox(
                expanded = expandedCity,
                onExpandedChange = { expandedCity = !expandedCity },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) { OutlinedTextField(
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
                    onDismissRequest = { expandedCity = false }
                ) {
                    // 🔹 Primero las ciudades del país seleccionado
                    selectedCountry.cities.forEach { city ->
                        DropdownMenuItem(
                            text = { Text(city) },
                            onClick = {
                                selectedCity = city
                                expandedCity = false
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
                                    text = { Text("${city} (${country.name})") },
                                    onClick = {
                                        selectedCity = city
                                        selectedCountry = country
                                        expandedCity = false
                                    }
                                )
                            }
                        }
                }
            }


            Spacer(Modifier.height(8.dp))

            // Mostrar error
            if (authState.error != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                ) {
                    Text(
                        text = authState.error,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }

            // Botón Registro
            Button(
                onClick = { onEmailRegister(
                    name,
                    lastName,
                    email,
                    password,
                    selectedCountry.name,
                    selectedCity,
                    selectedCountry.code + phoneNumber
                ) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0056B3),
                    contentColor = Color.White
                )
            ) {
                Text("Registrar", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewFormRegister() {
    MaterialTheme {
        FormRegister(
            authState = AuthState(),
            isRegistering = false,
            onEmailRegister = { _, _, _, _, _, _, _ -> },
            onGoogleLogin = {},
            onClearError = {}
        )
    }
}

