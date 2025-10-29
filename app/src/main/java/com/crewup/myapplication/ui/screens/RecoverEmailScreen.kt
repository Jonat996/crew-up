package com.crewup.myapplication.ui.screens.password

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.crewup.myapplication.ui.components.header.HeaderLogo
import com.crewup.myapplication.ui.components.sections.RecoverEmailSection
import com.crewup.myapplication.ui.layout.MainLayout
import com.crewup.myapplication.viewmodel.AuthViewModel
import com.crewup.myapplication.viewmodel.AuthState

@Composable
fun RecoverEmailScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    authState: AuthState
) {
    MainLayout(
        header = {
            HeaderLogo(title = "Recuperar Contraseña")
        },
        content = {
            RecoverEmailSection(
                navController = navController,
                authViewModel = authViewModel,
                authState = authState
            )
        }
    )
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewRecoverEmailScreen() {
    val navController = rememberNavController()
    RecoverEmailScreen(
        navController = navController,
        authViewModel = AuthViewModel(),
        authState = AuthState()
    )
}
