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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.crewup.myapplication.ui.components.header.HeaderLogo
import com.crewup.myapplication.ui.components.sections.plans.*
import com.crewup.myapplication.ui.layout.MainLayout
import com.crewup.myapplication.viewmodel.CreatePlanViewModel
import java.time.LocalTime
import java.time.ZoneId
import com.google.firebase.Timestamp
import java.util.Date

/**
 * Pantalla del flujo de edición de planes.
 * Reutiliza el CreatePlanViewModel en modo edición.
 */
@Composable
fun EditPlanFlowScreen(
    navController: NavController,
    planId: String,
    viewModel: CreatePlanViewModel = viewModel()
) {
    val planState by viewModel.planState.collectAsState()
    val currentStep by viewModel.currentStep.collectAsState()
    val error by viewModel.error.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val creationComplete by viewModel.creationComplete.collectAsState()
    val userCountry by viewModel.userCountry.collectAsState()
    val userCity by viewModel.userCity.collectAsState()
    val isEditMode by viewModel.isEditMode.collectAsState()

    // Inicializar el plan para edición
    LaunchedEffect(planId) {
        viewModel.initializeEditPlan(planId)
    }

    // Launcher para seleccionar imagen
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.uploadImage(it) }
    }

    // Navegar de regreso cuando se complete la edición
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
            HeaderLogo(
                title = stepTitles.getOrNull(currentStep) ?: "Editar Plan",
                navController = navController
            )
        },
        content = {
            Spacer(Modifier.height(4.dp))
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
                                val instant = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()
                                val timestamp = Timestamp(Date.from(instant))
                                viewModel.updateDateTime(timestamp, planState.time)
                            },
                            onTimeSelected = { localTime ->
                                val timeString = String.format("%02d:%02d", localTime.hour, localTime.minute)
                                viewModel.updateDateTime(planState.date, timeString)
                            }
                        )

                        3 -> LocationPlanSection(
                            initialQuery = planState.location.fullAddress,
                            userCountry = userCountry,
                            userCity = userCity,
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
                EditNavigationButtons(
                    currentStep = currentStep,
                    isEditMode = isEditMode,
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
private fun EditNavigationButtons(
    currentStep: Int,
    isEditMode: Boolean,
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

            // Botón Siguiente/Actualizar
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
                    text = if (currentStep == 4) {
                        if (isEditMode) "Actualizar!" else "Crear!"
                    } else {
                        "Siguiente"
                    },
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
