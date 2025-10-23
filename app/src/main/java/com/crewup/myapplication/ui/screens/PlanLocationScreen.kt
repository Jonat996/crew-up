package com.crewup.myapplication.ui.screens


import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.crewup.myapplication.ui.components.Header
import com.crewup.myapplication.ui.components.header.HeaderBase
import com.crewup.myapplication.ui.components.sections.plans.LocationPlanSection
import com.crewup.myapplication.ui.layout.MainLayout

@Composable
fun PlanLocationScreen(
    navController: NavController
){

    MainLayout(
        header = {
            HeaderBase(navController = navController) {
                Header("¿Dónde será tu plan?")
            }
        },
        content = {
            LocationPlanSection()
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewPlanLocation() {
    val navController = rememberNavController()
    PlanLocationScreen(navController = navController)
}