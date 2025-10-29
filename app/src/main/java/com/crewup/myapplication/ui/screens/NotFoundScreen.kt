package com.crewup.myapplication.ui.screens.error

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.crewup.myapplication.ui.components.sections.NotFoundSection
import com.crewup.myapplication.ui.layout.MainLayout

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotFoundScreen(navController: NavController) {
    MainLayout(
        header = {}, // 🔹 Header vacío
        content = {
            NotFoundSection(
                onBackToHome = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewNotFoundScreen() {
    NotFoundScreen(rememberNavController())
}
