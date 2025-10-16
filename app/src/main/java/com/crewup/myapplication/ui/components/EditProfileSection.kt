package com.crewup.myapplication.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crewup.myapplication.ui.components.forminputs.CityInput
import com.crewup.myapplication.ui.components.forminputs.EmailInput
import com.crewup.myapplication.ui.components.forminputs.GenderInput
import com.crewup.myapplication.ui.components.forminputs.NameAndLastNameInput
import com.crewup.myapplication.ui.components.forminputs.OccupationInput

@Composable
fun EditProfileSection(){
    var name by remember { mutableStateOf("Jonatan") }
    var lastName by remember { mutableStateOf("Urrutia") }
    var email by remember { mutableStateOf("jonatan@example.com") }
    var occupation by remember { mutableStateOf("Android Developer") }
    var gender by remember { mutableStateOf("Masculino") }

    var selectedCountry by remember { mutableStateOf(countriesList[0]) }
    var selectedCity by remember { mutableStateOf(selectedCountry.cities.first()) }
    var expandedCity by remember { mutableStateOf(false) }


    Column(modifier = Modifier.padding(16.dp)) {
        NameAndLastNameInput(
            name = name,
            onNameChange = { name = it },
            lastName = lastName,
            onLastNameChange = { lastName = it }
        )
        EmailInput(
            email = email,
            onEmailChange = { email = it },
            onClearError = {}
        )
        CityInput(
            selectedCity = selectedCity,
            onCitySelected = { selectedCity = it },
            expandedCity = expandedCity,
            onExpandedCityChange = { expandedCity = it },
            selectedCountry = selectedCountry,
            onCountrySelected = {
                selectedCountry = it
                selectedCity = it.cities.first()
            },
            countries = countriesList
        )
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
            onClick = { /* Handle save changes */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0056B3),
                contentColor = Color.White
            )
        ) {
            Text("Guardar cambios", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}
