package com.crewup.myapplication.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.crewup.myapplication.R
import com.crewup.myapplication.ui.components.Header
import com.crewup.myapplication.ui.components.header.HeaderBase
import com.crewup.myapplication.ui.components.sections.plans.DatePlanSection
import com.crewup.myapplication.ui.layout.MainLayout

@Composable
fun PlanDateScreen(
    navController: NavController
){

    MainLayout(
        header = {
            HeaderBase(navController = navController) {
                Header("¿Cuándo será?")
            }
        },
        content = {
            DatePlanSection()

        }
    )
}

@Preview(showBackground = true)
@Composable
fun PlanDateScreenPreview() {
    val navController = rememberNavController()
    PlanDateScreen(navController = navController)
}