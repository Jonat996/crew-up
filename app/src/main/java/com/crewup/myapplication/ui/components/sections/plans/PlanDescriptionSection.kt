package com.crewup.myapplication.ui.components.sections.plans

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.crewup.myapplication.ui.components.ImageUploadComponent
import com.crewup.myapplication.ui.components.InputWrapper
import com.crewup.myapplication.viewmodel.PlanViewModel
import java.util.UUID

/**
 * Sección para describir un plan, utilizando componentes de entrada personalizados.
 *
 * @param modifier Modificador para la sección.
 */
@Composable
fun PlanDescriptionSection(modifier: Modifier = Modifier) {
    // Instancia el ViewModel usando viewModel() de Compose
    val viewModel: PlanViewModel = viewModel()
    // Genera un planId único
    val planId = remember { UUID.randomUUID().toString() }
    var planName by remember { mutableStateOf("") }
    var planDescription by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    Column(modifier = modifier) {
        // Integra ImageUploadComponent con el ViewModel y planId local
        ImageUploadComponent(
            viewModel = viewModel,
            planId = planId
        )

        // Primer InputWrapper para el nombre del plan
        InputWrapper(
            title = "Pónle nombre al plan!",
            description = "Elije un nombre que sea claro para que las personas se unan a tu plan.",
            detail = "Plan Crew Up",
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = planName,
                onValueChange = { planName = it },
                label = { Text("Plan Crew Up") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF165BB0),
                    unfocusedBorderColor = Color(0xFF165BB0),
                    focusedLabelColor = Color(0xFF165BB0),
                    unfocusedLabelColor = Color(0xFF165BB0)
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                singleLine = true
            )
        }

        // Segundo InputWrapper para la descripción del plan
        InputWrapper(
            title = "Describenos tu plan!",
            description = "¿Qué deberían saber para unirse a tu plan?",
            detail = "Agrega una descripción",
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = planDescription,
                onValueChange = { planDescription = it },
                label = { Text("Agrega una descripción") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                maxLines = 6
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewPlanDescriptionSection() {
    PlanDescriptionSection(
        modifier = Modifier
    )
}