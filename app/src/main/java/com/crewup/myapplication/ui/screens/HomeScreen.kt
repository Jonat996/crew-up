package com.crewup.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.crewup.myapplication.ui.navigation.Routes
import com.google.firebase.auth.FirebaseUser
import com.crewup.myapplication.models.Plan
import com.crewup.myapplication.R
import com.crewup.myapplication.ui.components.home.ActivityOption
import com.crewup.myapplication.ui.components.home.ActivitySelector
import com.crewup.myapplication.ui.components.home.SearchHeaderBar
import com.crewup.myapplication.ui.components.sections.plans.VerificationSection
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun HomeScreen(
    user: FirebaseUser?,
    navController: NavController
) {
    var query by remember { mutableStateOf("") }
    var selectedActivity by remember { mutableStateOf<String?>(null) }
    val scrollState = rememberScrollState()
    var plans by remember { mutableStateOf<List<Plan>>(emptyList()) }
    var error by remember { mutableStateOf<String?>(null) }



    val activityOptions = listOf(
        ActivityOption("Conversar", R.drawable.ic_home_coffe, onClick = {}),
        ActivityOption("Chill", R.drawable.ic_home_beach, onClick = {}),
        ActivityOption("Natación", R.drawable.ic_home_pool, onClick = {}),
        ActivityOption("Fútbol", R.drawable.ic_home_soccer, onClick = {}),
        ActivityOption(
            "Crea",
            R.drawable.ic_home_more,
            onClick = { navController.navigate(Routes.CreatePlan.route) })
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(top = 20.dp)
            .background(Color(0xFFF7F7F7)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // COMPONENTE 1: SearchHeaderBar
        SearchHeaderBar(
            query = query,
            onSearchClick = { /* abrir búsqueda */ },
            onFilterClick = { /* abrir filtro */ },
            onProfileClick = { navController.navigate(Routes.Profile.route) }
        )

        // Espacio entre los componentes
        Spacer(modifier = Modifier.height(16.dp))

        // COMPONENTE 2: ActivitySelector
        ActivitySelector(
            activities = activityOptions,
            selectedActivity = selectedActivity,
            onActivitySelected = { selectedActivity = it }
        )
        LaunchedEffect(selectedActivity) {
            selectedActivity?.let { categoria ->
                FirebaseFirestore.getInstance()
                    .collection("plans")
                    .whereEqualTo("categoria", categoria)
                    .get()
                    .addOnSuccessListener { result ->
                        plans = result.documents.mapNotNull { it.toObject(Plan::class.java) }
                    }
                    .addOnFailureListener { exception ->
                        error = exception.message
                    }
            }
        }
        Spacer( modifier = Modifier.height(19.dp))




    }
}

