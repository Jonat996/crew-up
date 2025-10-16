package com.crewup.myapplication.ui.components.forminputs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NameAndLastNameInput(
    name: String,
    onNameChange: (String) -> Unit,
    lastName: String,
    onLastNameChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Nombre") },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(10.dp)

        )
        OutlinedTextField(
            value = lastName,
            onValueChange = onLastNameChange,
            label = { Text("Apellido") },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(10.dp)
        )
    }
}