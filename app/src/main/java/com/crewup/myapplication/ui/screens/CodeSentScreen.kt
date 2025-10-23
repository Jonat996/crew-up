package com.crewup.myapplication.ui.screens.password

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.crewup.myapplication.ui.components.header.HeaderLogo
import com.crewup.myapplication.ui.layout.MainLayout

@Composable
fun CodeSentScreen(navController: NavController) {
    MainLayout(
        header = {
            HeaderLogo(title = "Código enviado")
        },
        content = {
            CodeSentSection(navController = navController)
        }
    )
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewCodeSentScreenFull() {
    val navController = rememberNavController()
    CodeSentScreen(navController)
}
