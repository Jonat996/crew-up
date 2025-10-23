package com.crewup.myapplication.ui.components.sections.plans

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.crewup.myapplication.ui.components.InputWrapper

/**
 * Sección para describir un plan, utilizando componentes de entrada personalizados.
 *
 * @param modifier Modificador para la sección.
 * @param title Título actual del plan.
 * @param description Descripción actual del plan.
 * @param onTitleChange Callback cuando cambia el título.
 * @param onDescriptionChange Callback cuando cambia la descripción.
 * @param planId ID del plan para subir imagen (opcional).
 * @param viewModel ViewModel opcional para la subida de imágenes.
 */
@Composable
fun PlanDescriptionSection(
    modifier: Modifier = Modifier,
    title: String = "",
    description: String = "",
    imageUrl: String = "",
    onTitleChange: (String) -> Unit = {},
    onDescriptionChange: (String) -> Unit = {},
    onImageSelected: (Uri) -> Unit = {},
    isLoadingImage: Boolean = false
) {
    var planName by remember(title) { mutableStateOf(title) }
    var planDescription by remember(description) { mutableStateOf(description) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onImageSelected(it) }
    }

    Column(modifier = modifier) {
        // IMAGEN PRIMERO - Card para seleccionar/mostrar imagen
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clickable(onClick = { imagePickerLauncher.launch("image/*") }),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (imageUrl.isNotBlank()) Color.Transparent else Color(0xFFF5F5F5)
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (imageUrl.isNotBlank()) {
                    // Mostrar la imagen cargada
                    Image(
                        painter = rememberAsyncImagePainter(imageUrl),
                        contentDescription = "Imagen del plan",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // Placeholder cuando no hay imagen
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(android.R.drawable.ic_menu_camera),
                            contentDescription = "Subir imagen",
                            modifier = Modifier.size(48.dp),
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Toca para agregar una imagen",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }

                // Indicador de carga sobre la imagen
                if (isLoadingImage) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }
            }
        }

        // Primer InputWrapper para el nombre del plan
        InputWrapper(
            title = "Pónle nombre al plan!",
            description = "Elije un nombre que sea claro para que las personas se unan a tu plan.",
            detail = "Plan Crew Up",
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = planName,
                onValueChange = {
                    planName = it
                    onTitleChange(it)
                },
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
                onValueChange = {
                    planDescription = it
                    onDescriptionChange(it)
                },
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