package com.crewup.myapplication.ui.components.sections

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.crewup.myapplication.ui.components.forminputs.CityInput
import com.crewup.myapplication.ui.components.forminputs.EmailInput
import com.crewup.myapplication.ui.components.forminputs.GenderInput
import com.crewup.myapplication.ui.components.forminputs.NameAndLastNameInput
import com.crewup.myapplication.ui.components.forminputs.OccupationInput
import com.crewup.myapplication.viewmodel.UserViewModel
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.crewup.myapplication.R
import com.crewup.myapplication.ui.components.countriesList


@Composable
fun EditProfileSection(){
    val userViewModel: UserViewModel = viewModel()
    val userState by userViewModel.userState.collectAsState()

    // Estado local para los campos del formulario
    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var occupation by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var selectedCity by remember { mutableStateOf("") }
    var selectedCountryName by remember { mutableStateOf("") }
    var expandedCity by remember { mutableStateOf(false) }

    // Log para depuración inicial
    LaunchedEffect(Unit) {
        println("=== EditProfileSection iniciado ===")
        println("UserState inicial - isLoading: ${userState.isLoading}, user: ${userState.user}, error: ${userState.error}")
    }

    // Rellena el estado local cuando los datos del usuario se cargan
    LaunchedEffect(userState.user) {
        println("=== LaunchedEffect disparado ===")
        println("UserState actual - isLoading: ${userState.isLoading}")
        println("Datos de usuario recibidos en la UI: ${userState.user}")
        println("User name: ${userState.user?.name}")
        println("User email: ${userState.user?.email}")

        userState.user?.let { user ->
            println("Actualizando campos del formulario...")
            name = user.name
            lastName = user.lastName
            email = user.email
            occupation = user.occupation
            gender = user.gender
            selectedCity = user.city
            selectedCountryName = user.country
            println("Campos actualizados - name: $name, email: $email")
        } ?: println("userState.user es null")
    }

    val selectedCountry = remember(selectedCountryName) {
        countriesList.find { it.name == selectedCountryName } ?: countriesList.firstOrNull()
    }

    if (userState.isLoading && userState.user == null) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
        }
    } else if (userState.error != null) {
        Text(text = stringResource(R.string.error_prefix, userState.error ?: ""), color = Color.Red, modifier = Modifier.padding(16.dp))
    } else {
        Column(modifier = Modifier.padding(16.dp)) {
            NameAndLastNameInput(
                name = name,
                onNameChange = { name = it },
                lastName = lastName,
                onLastNameChange = { lastName = it }
            )
            if (selectedCountry != null) {
                CityInput(
                    selectedCity = selectedCity,
                    onCitySelected = { selectedCity = it },
                    expandedCity = expandedCity,
                    onExpandedCityChange = { expandedCity = it },
                    selectedCountry = selectedCountry,
                    onCountrySelected = { country ->
                        selectedCountryName = country.name
                        selectedCity = country.cities.first()
                    },
                    countries = countriesList
                )
            }
            OccupationInput(
                occupation = occupation,
                onOccupationChange = { occupation = it }
            )
            GenderInput(
                gender = gender,
                onGenderChange = { gender = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    userState.user?.let { currentUser ->
                        userViewModel.updateUserProfile(
                            name = if (name != currentUser.name) name else null,
                            lastName = if (lastName != currentUser.lastName) lastName else null,
                            email = if (email != currentUser.email) email else null,
                            occupation = if (occupation != currentUser.occupation) occupation else null,
                            gender = if (gender != currentUser.gender) gender else null,
                            city = if (selectedCity != currentUser.city) selectedCity else null,
                            country = if (selectedCountryName != currentUser.country) selectedCountryName else null
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0056B3),
                    contentColor = Color.White
                )
            ) {
                if (userState.isLoading) {
                    CircularProgressIndicator(color = Color.White)
                } else {
                    Text(stringResource(R.string.save_changes), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Preview
@Composable
fun EditProfileSectionPreview(){
    EditProfileSection()
}