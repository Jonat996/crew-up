package com.crewup.myapplication.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.crewup.myapplication.auth.GoogleAuth
import com.crewup.myapplication.ui.screens.HomeScreen
import com.crewup.myapplication.ui.screens.LoginScreen
import com.crewup.myapplication.ui.screens.ProfileScreen
import com.crewup.myapplication.viewmodel.AuthViewModel
import com.crewup.myapplication.viewmodel.AuthState
import kotlinx.coroutines.launch

@Composable
fun AppNavigation(
    authState: AuthState,
    authViewModel: AuthViewModel,
    googleAuth: GoogleAuth
) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(authState.isAuthenticated) {
        if (authState.isAuthenticated) {
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        } else {
            navController.navigate("login") {
                popUpTo("home") { inclusive = true }
            }
        }
    }

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                authState = authState,
                onEmailLogin = { email, password ->
                    authViewModel.signInWithEmailPassword(email, password)
                },
                onEmailRegister = { email, password ->
                    authViewModel.registerWithEmailPassword(email, password)
                },
                onGoogleLogin = {
                    coroutineScope.launch {
                        val result = googleAuth.signInWithGoogle()
                        result.onFailure {
                            println("❌ Google Sign-In error: ${it.message}")
                        }
                    }
                },
                onClearError = {
                    authViewModel.clearError()
                }
            )
        }

        composable("home") {
            HomeScreen(
                user = authState.user,
                navController = navController
            )
        }

        composable("profile") {
            ProfileScreen(
                navController = navController,
                onSignOut = { authViewModel.signOut() }
            )
        }

    }
}