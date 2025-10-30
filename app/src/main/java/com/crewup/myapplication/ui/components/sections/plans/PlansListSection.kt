package com.crewup.myapplication.ui.components.sections.plans

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.crewup.myapplication.models.Plan
import com.crewup.myapplication.ui.components.ConfirmationType
import com.crewup.myapplication.ui.components.PlanCard
import com.crewup.myapplication.ui.components.PlanConfirmationCard
import com.crewup.myapplication.viewmodel.PlansListViewModel
import com.crewup.myapplication.viewmodel.PlanViewModel
import com.crewup.myapplication.viewmodel.UserViewModel

/**
 * Sección que muestra una lista de planes disponibles.
 *
 * Características:
 * - Prioriza planes de la ciudad del usuario
 * - Muestra diferentes opciones según el estado del usuario:
 *   - Creador: Editar, Eliminar
 *   - Participante: Chat, Salir
 *   - No unido: Unirse
 * - Permite filtrar por tags (opcional)
 * - Maneja estados de carga, error y vacío
 */
@Composable
fun PlansListSection(
    onPlanClick: (String) -> Unit,
    onChatClick: (String) -> Unit,
    onEditClick: (String) -> Unit,
    selectedTags: List<String> = emptyList(),
    userViewModel: UserViewModel = viewModel(),
    plansListViewModel: PlansListViewModel = viewModel(),
    planViewModel: PlanViewModel = viewModel()
) {
    val plans by plansListViewModel.plans.collectAsState()
    val isLoading by plansListViewModel.isLoading.collectAsState()
    val error by plansListViewModel.error.collectAsState()
    val currentUserId by plansListViewModel.currentUserId.collectAsState()

    val userState by userViewModel.userState.collectAsState()
    val currentUser = userState.user
    val userCity = currentUser?.city

    val joinState by planViewModel.joinState.collectAsState()
    val leaveState by planViewModel.leaveState.collectAsState()
    val deleteState by planViewModel.deleteState.collectAsState()

    // Estados para controlar los diálogos de confirmación
    var planToLeave by remember { mutableStateOf<Plan?>(null) }
    var showLeaveDialog by remember { mutableStateOf(false) }
    var planToDelete by remember { mutableStateOf<Plan?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Estado para controlar el diálogo de detalle del plan
    var selectedPlan by remember { mutableStateOf<Plan?>(null) }
    var showPlanDetail by remember { mutableStateOf(false) }

    // Cargar planes cuando se monta el componente o cambian los tags
    LaunchedEffect(selectedTags) {
        if (selectedTags.isEmpty()) {
            plansListViewModel.loadPlans()
        } else {
            plansListViewModel.loadPlans(tags = selectedTags)
        }
    }

    // Recargar planes cuando se une o sale de un plan
    LaunchedEffect(joinState) {
        if (joinState is com.crewup.myapplication.viewmodel.ActionState.Success) {
            // Cerrar el diálogo de detalle si está abierto
            showPlanDetail = false
            selectedPlan = null
            plansListViewModel.loadPlans(tags = selectedTags)
            planViewModel.resetJoinState()
        }
    }

    LaunchedEffect(leaveState) {
        if (leaveState is com.crewup.myapplication.viewmodel.ActionState.Success) {
            showLeaveDialog = false
            planToLeave = null
            // Cerrar el diálogo de detalle si está abierto
            showPlanDetail = false
            selectedPlan = null
            plansListViewModel.loadPlans(tags = selectedTags)
            planViewModel.resetLeaveState()
        }
    }

    // Recargar planes cuando se elimina un plan
    LaunchedEffect(deleteState) {
        if (deleteState is com.crewup.myapplication.viewmodel.ActionState.Success) {
            showDeleteDialog = false
            planToDelete = null
            // Cerrar el diálogo de detalle si está abierto
            showPlanDetail = false
            selectedPlan = null
            plansListViewModel.loadPlans(tags = selectedTags)
            planViewModel.resetDeleteState()
        }
    }

    // Ordenar planes: primero los de la ciudad del usuario
    val sortedPlans = remember(plans, userCity) {
        if (userCity.isNullOrBlank()) {
            plans
        } else {
            plans.sortedByDescending { plan ->
                // Priorizar si la ciudad del plan coincide con la ciudad del usuario
                val cityMatch = plan.location.city?.contains(userCity, ignoreCase = true) == true
                val nameMatch = plan.location.name.contains(userCity, ignoreCase = true)
                val addressMatch = plan.location.fullAddress.contains(userCity, ignoreCase = true)

                // Prioridad: 1. city exacta, 2. en el nombre, 3. en dirección completa
                when {
                    cityMatch -> true
                    nameMatch || addressMatch -> true
                    else -> false
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Título de la sección
        Text(
            text = if (selectedTags.isEmpty()) "Planes disponibles" else "Planes filtrados",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        // Subtítulo con ciudad si aplica
        if (!userCity.isNullOrBlank()) {
            Text(
                text = "Priorizando planes en $userCity",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }

        when {
            isLoading -> {
                // Estado de carga
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF0056B3))
                }
            }
            error != null -> {
                // Estado de error
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Error al cargar planes",
                            color = Color.Red,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = error ?: "Error desconocido",
                            color = Color.Gray,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                plansListViewModel.loadPlans(tags = selectedTags)
                            },
                            colors = ButtonDefaults.buttonColors(Color(0xFF0056B3))
                        ) {
                            Text("Reintentar")
                        }
                    }
                }
            }
            sortedPlans.isEmpty() -> {
                // Estado vacío
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (selectedTags.isEmpty()) {
                            "No hay planes disponibles en este momento"
                        } else {
                            "No hay planes que coincidan con los filtros seleccionados"
                        },
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }
            else -> {
                // Lista de planes
                Column {
                    sortedPlans.forEach { plan ->
                        PlanCard(
                            plan = plan,
                            currentUserUid = currentUserId ?: "",
                            onPlanClick = { planId ->
                                // Abrir diálogo de detalle del plan
                                selectedPlan = plan
                                showPlanDetail = true
                            },
                            onJoinClick = { planId ->
                                planViewModel.joinPlan(planId)
                            },
                            onLeaveClick = { planId ->
                                // Mostrar diálogo de confirmación para salir
                                planToLeave = plan
                                showLeaveDialog = true
                            },
                            onChatClick = { planId ->
                                onChatClick(planId)
                            },
                            onEditClick = { planId ->
                                onEditClick(planId)
                            },
                            onDeleteClick = { planId ->
                                // Mostrar diálogo de confirmación para eliminar
                                planToDelete = plan
                                showDeleteDialog = true
                            }
                        )
                    }
                }
            }
        }
    }

    // Diálogo de carga al unirse a un plan
    if (joinState is com.crewup.myapplication.viewmodel.ActionState.Loading) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Uniéndote al plan...") },
            text = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            },
            confirmButton = { }
        )
    }

    // Diálogo de error al unirse
    if (joinState is com.crewup.myapplication.viewmodel.ActionState.Error) {
        val errorMessage = (joinState as com.crewup.myapplication.viewmodel.ActionState.Error).message
        AlertDialog(
            onDismissRequest = { planViewModel.resetJoinState() },
            title = { Text("Error") },
            text = { Text(errorMessage) },
            confirmButton = {
                Button(onClick = { planViewModel.resetJoinState() }) {
                    Text("Aceptar")
                }
            }
        )
    }

    // Diálogo de carga al salir de un plan
    if (leaveState is com.crewup.myapplication.viewmodel.ActionState.Loading) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Saliendo del plan...") },
            text = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            },
            confirmButton = { }
        )
    }

    // Diálogo de error al salir
    if (leaveState is com.crewup.myapplication.viewmodel.ActionState.Error) {
        val errorMessage = (leaveState as com.crewup.myapplication.viewmodel.ActionState.Error).message
        AlertDialog(
            onDismissRequest = {
                planViewModel.resetLeaveState()
                showLeaveDialog = false
                planToLeave = null
            },
            title = { Text("Error") },
            text = { Text(errorMessage) },
            confirmButton = {
                Button(onClick = {
                    planViewModel.resetLeaveState()
                    showLeaveDialog = false
                    planToLeave = null
                }) {
                    Text("Aceptar")
                }
            }
        )
    }

    // Diálogo de confirmación para salir del plan usando PlanConfirmationCard
    if (showLeaveDialog && planToLeave != null) {
        Dialog(
            onDismissRequest = {
                showLeaveDialog = false
                planToLeave = null
            }
        ) {
            PlanConfirmationCard(
                plan = planToLeave!!,
                type = ConfirmationType.LEAVE,
                onConfirm = {
                    // Ejecutar la salida del plan
                    currentUserId?.let { userId ->
                        planViewModel.leavePlan(planToLeave!!.id, userId)
                    }
                },
                onDismiss = {
                    // Cerrar el diálogo sin salir
                    showLeaveDialog = false
                    planToLeave = null
                }
            )
        }
    }

    // Diálogo de confirmación para eliminar plan usando PlanConfirmationCard
    if (showDeleteDialog && planToDelete != null) {
        Dialog(
            onDismissRequest = {
                showDeleteDialog = false
                planToDelete = null
            }
        ) {
            PlanConfirmationCard(
                plan = planToDelete!!,
                type = ConfirmationType.DELETE,
                onConfirm = {
                    // Ejecutar la eliminación
                    planViewModel.deletePlan(planToDelete!!.id)
                },
                onDismiss = {
                    // Cerrar el diálogo sin eliminar
                    showDeleteDialog = false
                    planToDelete = null
                }
            )
        }
    }

    // Diálogo de carga durante la eliminación
    if (deleteState is com.crewup.myapplication.viewmodel.ActionState.Loading) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Eliminando plan...") },
            text = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            },
            confirmButton = { }
        )
    }

    // Diálogo de error al eliminar
    if (deleteState is com.crewup.myapplication.viewmodel.ActionState.Error) {
        val errorMessage = (deleteState as com.crewup.myapplication.viewmodel.ActionState.Error).message
        AlertDialog(
            onDismissRequest = {
                planViewModel.resetDeleteState()
                showDeleteDialog = false
                planToDelete = null
            },
            title = { Text("Error") },
            text = { Text(errorMessage) },
            confirmButton = {
                Button(onClick = {
                    planViewModel.resetDeleteState()
                    showDeleteDialog = false
                    planToDelete = null
                }) {
                    Text("Aceptar")
                }
            }
        )
    }

    // Diálogo de detalle del plan (card expandida)
    if (showPlanDetail && selectedPlan != null) {
        Dialog(
            onDismissRequest = {
                showPlanDetail = false
                selectedPlan = null
            }
        ) {
            PlanDetailCard(
                plan = selectedPlan!!,
                currentUserUid = currentUserId ?: "",
                onJoinClick = { planId ->
                    planViewModel.joinPlan(planId)
                },
                onLeaveClick = { planId ->
                    // Cerrar detalle y mostrar confirmación para salir
                    showPlanDetail = false
                    planToLeave = selectedPlan
                    showLeaveDialog = true
                },
                onChatClick = { planId ->
                    // Cerrar detalle y navegar al chat
                    showPlanDetail = false
                    selectedPlan = null
                    onChatClick(planId)
                },
                onEditClick = { planId ->
                    // Cerrar detalle y navegar a editar
                    showPlanDetail = false
                    selectedPlan = null
                    onEditClick(planId)
                },
                onDeleteClick = { planId ->
                    // Cerrar detalle y mostrar confirmación para eliminar
                    showPlanDetail = false
                    planToDelete = selectedPlan
                    showDeleteDialog = true
                },
                onDismiss = {
                    showPlanDetail = false
                    selectedPlan = null
                }
            )
        }
    }
}
