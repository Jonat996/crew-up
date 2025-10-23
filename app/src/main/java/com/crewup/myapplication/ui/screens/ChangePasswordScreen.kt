package com.crewup.myapplication.ui.screens.password

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.crewup.myapplication.ui.components.header.HeaderLogo
import com.crewup.myapplication.ui.layout.MainLayout
import com.crewup.myapplication.ui.screens.password.ChangePasswordSection

@Composable
fun ChangePasswordScreen(navController: NavController) {
    MainLayout(
        header = {
            HeaderLogo(title = "Nueva Contraseña")
        },
        content = {
            ChangePasswordSection(navController = navController)
        }
    )
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewChangePasswordScreen() {
    val navController = rememberNavController()
    ChangePasswordScreen(navController)
}
