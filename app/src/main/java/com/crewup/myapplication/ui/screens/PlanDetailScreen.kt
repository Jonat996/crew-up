package com.crewup.myapplication.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.crewup.myapplication.models.mockPlanDetail
import com.crewup.myapplication.ui.components.Header
import com.crewup.myapplication.ui.components.header.HeaderBase
import com.crewup.myapplication.ui.components.sections.plans.DetailPlanSection
import com.crewup.myapplication.ui.layout.MainLayout

@Composable
fun PlanDetailScreen(
    navController: NavController
){

    MainLayout(
        header = {
            HeaderBase(navController = navController) {
                Header("Verifica tu plan")
            }
        },
        content = {
            DetailPlanSection(plan = mockPlanDetail)

        }
    )
}

@Preview(showBackground = true)
@Composable
fun PlanDetailScreenPreview() {
    val navController = rememberNavController()
    PlanDetailScreen(navController = navController)
}