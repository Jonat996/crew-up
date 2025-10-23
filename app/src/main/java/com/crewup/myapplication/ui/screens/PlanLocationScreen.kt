package com.crewup.myapplication.ui.screens


import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.crewup.myapplication.ui.components.Header
import com.crewup.myapplication.ui.components.header.HeaderBase
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
            PlanLocationScreen(navController)

        }
    )
}