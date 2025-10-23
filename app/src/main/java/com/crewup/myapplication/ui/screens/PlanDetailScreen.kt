package com.crewup.myapplication.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.crewup.myapplication.models.mockPlanDetail
import com.crewup.myapplication.ui.components.Header
import com.crewup.myapplication.ui.components.header.HeaderBase
import com.crewup.myapplication.ui.components.plans.DetailPlan
import com.crewup.myapplication.ui.components.sections.plans.DatePlanSection
import com.crewup.myapplication.ui.components.sections.plans.DetailPlanSection
import com.crewup.myapplication.ui.layout.MainLayout

@Composable
fun PlanDetailScreen(
    navController: NavController
){

    MainLayout(
        header = {
            HeaderBase(navController = navController) {
                Header("¿Cuándo será?")
            }
        },
        content = {
            DetailPlanSection(plan = mockPlanDetail)

        }
    )
}