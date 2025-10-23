package com.crewup.myapplication.ui.screens.password

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.crewup.myapplication.ui.components.header.HeaderLogo
import com.crewup.myapplication.ui.layout.MainLayout

@Composable
fun OtpVerificationScreen(navController: NavController) {
    MainLayout(
        header = {
            HeaderLogo(title = "Verificación")
        },
        content = {
            OtpVerificationSection(navController = navController)
        }
    )
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewOtpVerificationScreenFull() {
    val navController = rememberNavController()
    OtpVerificationScreen(navController)
}
