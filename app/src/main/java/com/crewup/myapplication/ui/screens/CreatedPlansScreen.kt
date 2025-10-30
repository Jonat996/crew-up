package com.crewup.myapplication.ui.screens

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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.crewup.myapplication.ui.components.PlanCard
import com.crewup.myapplication.ui.components.header.HeaderUserInfo
import com.crewup.myapplication.ui.navigation.Routes
import com.crewup.myapplication.viewmodel.AuthViewModel
import com.crewup.myapplication.viewmodel.PlanViewModel
import com.crewup.myapplication.viewmodel.PlansListViewModel
import com.crewup.myapplication.viewmodel.UserViewModel

/**
 * Pantalla que muestra los planes creados por el usuario actual.
 *
 * Incluye:
 * - HeaderUserInfo con la información del usuario
 * - Lista de planes creados usando PlanCard
 * - Acciones de editar y eliminar plan (solo para el creador)
 */
@Composable
fun CreatedPlansScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel(),
    plansListViewModel: PlansListViewModel = viewModel(),
    planViewModel: PlanViewModel = viewModel()
) {
    val authState by authViewModel.authState.collectAsState()
    val plans by plansListViewModel.plans.collectAsState()
    val isLoading by plansListViewModel.isLoading.collectAsState()
    val error by plansListViewModel.error.collectAsState()
    val currentUserId by plansListViewModel.currentUserId.collectAsState()
    val deleteState by planViewModel.deleteState.collectAsState()

    // Cargar planes del usuario cuando se monta la pantalla
    LaunchedEffect(Unit) {
        plansListViewModel.loadUserPlans()
    }

    // Manejar eliminación exitosa
    LaunchedEffect(deleteState) {
        if (deleteState is com.crewup.myapplication.viewmodel.ActionState.Success) {
            // Recargar la lista después de eliminar
            plansListViewModel.loadUserPlans()
            planViewModel.resetDeleteState()
        }
    }

    Scaffold { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Header como primer item
            item {
                Column {
                    HeaderUserInfo(
                        navController = navController,
                        authViewModel = authViewModel,
                        userViewModel = userViewModel
                    )

                    // Título de la sección
                    Text(
                        text = "Mis planes creados",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                    )
                }
            }

            when {
                isLoading -> {
                    item {
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
                }
                error != null -> {
                    item {
                        // Estado de error
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Error: $error",
                                    color = Color.Red,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = { plansListViewModel.loadUserPlans() },
                                    colors = ButtonDefaults.buttonColors(Color(0xFF0056B3))
                                ) {
                                    Text("Reintentar")
                                }
                            }
                        }
                    }
                }
                plans.isEmpty() -> {
                    item {
                        // Estado vacío
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "No has creado ningún plan aún",
                                    fontSize = 18.sp,
                                    color = Color.Gray,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = {
                                        // Navegar a crear plan
                                        navController.navigate("create_plan")
                                    },
                                    colors = ButtonDefaults.buttonColors(Color(0xFF0056B3))
                                ) {
                                    Text("Crear mi primer plan")
                                }
                            }
                        }
                    }
                }
                else -> {
                    // Lista de planes
                    items(plans) { plan ->
                        PlanCard(
                            plan = plan,
                            currentUserUid = currentUserId ?: "",
                            onPlanClick = { planId ->
                                // Navegar a detalle del plan
                                navController.navigate("planDetail/$planId")
                            },
                            onJoinClick = { planId ->
                                // El creador no puede unirse a su propio plan
                            },
                            onLeaveClick = { planId ->
                                // El creador no puede salir de su propio plan
                            },
                            onChatClick = { planId ->
                                // Navegar al chat del plan
                                navController.navigate("planChat/$planId")
                            },
                            onEditClick = { planId ->
                                // Navegar a editar plan
                                navController.navigate(Routes.EditPlan.createRoute(planId))
                            },
                            onDeleteClick = { planId ->
                                // Mostrar diálogo de confirmación
                                planViewModel.deletePlan(planId)
                            }
                        )
                    }
                }
            }
        }
    }

    // Diálogo de confirmación para eliminar (opcional, puedes agregar uno más elaborado)
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

    // Mostrar error si falla la eliminación
    if (deleteState is com.crewup.myapplication.viewmodel.ActionState.Error) {
        val errorMessage = (deleteState as com.crewup.myapplication.viewmodel.ActionState.Error).message
        AlertDialog(
            onDismissRequest = { planViewModel.resetDeleteState() },
            title = { Text("Error") },
            text = { Text(errorMessage) },
            confirmButton = {
                Button(onClick = { planViewModel.resetDeleteState() }) {
                    Text("Aceptar")
                }
            }
        )
    }
}
