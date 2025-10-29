package com.crewup.myapplication.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.crewup.myapplication.R
import com.crewup.myapplication.models.Plan
import com.crewup.myapplication.ui.components.Header
import com.crewup.myapplication.ui.components.header.HeaderBase
import com.crewup.myapplication.ui.components.header.HeaderLogo
import com.crewup.myapplication.ui.components.sections.plans.*
import com.crewup.myapplication.ui.layout.MainLayout
import com.crewup.myapplication.viewmodel.CreatePlanViewModel
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.util.Date
import java.util.Locale

/**
 * Pantalla del flujo de creación de planes.
 * Maneja la navegación entre los diferentes pasos.
 */
@Composable
fun CreatePlanFlowScreen(
    navController: NavController,
    viewModel: CreatePlanViewModel = viewModel()
) {
    val planState by viewModel.planState.collectAsState()
    val currentStep by viewModel.currentStep.collectAsState()
    val error by viewModel.error.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val creationComplete by viewModel.creationComplete.collectAsState()

    // Launcher para seleccionar imagen
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.uploadImage(it) }
    }

    // Navegar de regreso cuando se complete la creación
    LaunchedEffect(creationComplete) {
        if (creationComplete) {
            navController.popBackStack()
        }
    }

    val stepTitles = listOf(
        "¿Cuál es el plan?",
        "Filtremos un poco...",
        "¿Cuándo será?",
        "¿Dónde será?",
        "Verifica tu plan"
    )

    MainLayout(
        header = {
            HeaderLogo(stepTitles.getOrNull(currentStep) ?: "Crear Plan")
        },
        content = {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 80.dp)
                ) {
                    // Mostrar error si existe
                    error?.let { errorMessage ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
                        ) {
                            Text(
                                text = errorMessage,
                                color = Color(0xFFD32F2F),
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }

                    // Indicador de carga
                    if (isLoading) {
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth(),
                            color = Color(0xFF0056B3)
                        )
                    }

                    // Contenido del paso actual
                    when (currentStep) {
                        0 -> PlanDescriptionSection(
                            title = planState.title,
                            description = planState.description,
                            imageUrl = planState.imageUrl,
                            onTitleChange = { title ->
                                viewModel.updateTitleAndDescription(title, planState.description)
                            },
                            onDescriptionChange = { description ->
                                viewModel.updateTitleAndDescription(planState.title, description)
                            },
                            onImageSelected = { uri ->
                                viewModel.uploadImage(uri)
                            },
                            isLoadingImage = isLoading
                        )

                        1 -> PlanConfigurationSection(
                            selectedTags = planState.tags,
                            minAge = planState.minAge,
                            maxAge = planState.maxAge,
                            selectedGender = planState.gender,
                            onTagsChange = { tags ->
                                viewModel.updateFilters(tags, planState.minAge, planState.maxAge, planState.gender)
                            },
                            onAgeRangeChanged = { min, max ->
                                viewModel.updateFilters(planState.tags, min, max, planState.gender)
                            },
                            onGenderChanged = { gender ->
                                viewModel.updateFilters(planState.tags, planState.minAge, planState.maxAge, gender)
                            }
                        )

                        2 -> DatePlanSection(
                            selectedDate = planState.date?.toDate()?.let { date ->
                                date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                            },
                            selectedTime = planState.time.takeIf { it.isNotBlank() }?.let { timeString ->
                                try {
                                    // Intentar parsear el string de tiempo a LocalTime
                                    val parts = timeString.split(":")
                                    if (parts.size >= 2) {
                                        LocalTime.of(parts[0].toInt(), parts[1].toInt())
                                    } else {
                                        LocalTime.of(12, 0)
                                    }
                                } catch (e: Exception) {
                                    LocalTime.of(12, 0)
                                }
                            },
                            onDateSelected = { localDate ->
                                // Convertir LocalDate a Timestamp
                                val instant = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()
                                val timestamp = Timestamp(Date.from(instant))
                                viewModel.updateDateTime(timestamp, planState.time)
                            },
                            onTimeSelected = { localTime ->
                                // Convertir LocalTime a String
                                val timeString = String.format("%02d:%02d", localTime.hour, localTime.minute)
                                viewModel.updateDateTime(planState.date, timeString)
                            }
                        )

                        3 -> LocationPlanSection(
                            initialQuery = planState.location.name,
                            onLocationSelected = { location ->
                                viewModel.updateLocation(location)
                            }
                        )

                        4 -> VerificationSection(
                            plan = planState,
                            onModify = { step ->
                                viewModel.goToStep(step)
                            }
                        )
                    }
                }

                // Botones de navegación en la parte inferior
                NavigationButtons(
                    currentStep = currentStep,
                    onPrevious = { viewModel.previousStep() },
                    onNext = { viewModel.nextStep() },
                    onFinish = { viewModel.finishPlan() },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                )
            }
        }
    )
}

@Composable
private fun NavigationButtons(
    currentStep: Int,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onFinish: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Botón Atrás
            if (currentStep > 0) {
                OutlinedButton(
                    onClick = onPrevious,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .padding(end = 8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF0056B3)
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Atrás", fontWeight = FontWeight.SemiBold)
                }
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }

            // Botón Siguiente/Crear
            Button(
                onClick = if (currentStep == 4) onFinish else onNext,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
                    .padding(start = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0056B3),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = if (currentStep == 4) "Crear!" else "Siguiente",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

/**
 * Sección para subir imagen.
 */
@Composable
private fun ImageUploadSection(
    imageUrl: String,
    onImagePickerClick: () -> Unit,
    isLoading: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Agrega una foto para tu plan",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Una buena imagen ayuda a atraer más personas",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Card para la imagen
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clickable(onClick = onImagePickerClick),
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
                            modifier = Modifier.size(64.dp),
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Toca para seleccionar una imagen",
                            color = Color.Gray,
                            fontSize = 16.sp
                        )
                    }
                }

                // Indicador de carga sobre la imagen
                if (isLoading) {
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

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para cambiar imagen
        if (imageUrl.isNotBlank()) {
            OutlinedButton(
                onClick = onImagePickerClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF0056B3)
                )
            ) {
                Text("Cambiar imagen")
            }
        }
    }
}

/**
 * Sección de verificación final.
 */
@Composable
private fun VerificationSection(
    plan: Plan,
    onModify: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Verifica tu plan",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                VerificationItem(
                    label = "Título",
                    value = plan.title,
                    onEdit = { onModify(0) }
                )
                Divider(modifier = Modifier.padding(vertical = 8.dp))

                VerificationItem(
                    label = "Descripción",
                    value = plan.description,
                    onEdit = { onModify(0) }
                )
                Divider(modifier = Modifier.padding(vertical = 8.dp))

                VerificationItem(
                    label = "Imagen",
                    value = if (plan.imageUrl.isNotBlank()) "✓ Cargada" else "No cargada",
                    onEdit = { onModify(0) }
                )
                Divider(modifier = Modifier.padding(vertical = 8.dp))

                VerificationItem(
                    label = "Categorías",
                    value = plan.tags.joinToString(", ").ifBlank { "Sin categorías" },
                    onEdit = { onModify(1) }
                )
                Divider(modifier = Modifier.padding(vertical = 8.dp))

                VerificationItem(
                    label = "Fecha",
                    value = plan.date?.toDate()?.let {
                        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(it)
                    } ?: "No especificada",
                    onEdit = { onModify(2) }
                )
                Divider(modifier = Modifier.padding(vertical = 8.dp))

                VerificationItem(
                    label = "Hora",
                    value = plan.time.ifBlank { "No especificada" },
                    onEdit = { onModify(2) }
                )
                Divider(modifier = Modifier.padding(vertical = 8.dp))

                VerificationItem(
                    label = "Ubicación",
                    value = plan.location.name.ifBlank { "No especificada" },
                    onEdit = { onModify(3) }
                )
            }
        }
    }
}

@Composable
private fun VerificationItem(
    label: String,
    value: String,
    onEdit: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        TextButton(onClick = onEdit) {
            Text("Editar", color = Color(0xFF0056B3))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCreatePlanFlowScreen() {
    val navController = rememberNavController()
    CreatePlanFlowScreen(navController = navController)
}
