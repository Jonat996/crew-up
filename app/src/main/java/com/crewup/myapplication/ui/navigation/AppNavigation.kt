package com.crewup.myapplication.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.crewup.myapplication.auth.GoogleAuth
import com.crewup.myapplication.ui.screens.CreatePlanFlowScreen
import com.crewup.myapplication.ui.screens.EditProfileScreen
import com.crewup.myapplication.ui.screens.HomeScreen
import com.crewup.myapplication.ui.screens.LoginScreen
import com.crewup.myapplication.ui.screens.NotificationsScreen
import com.crewup.myapplication.ui.screens.PlanDateScreen
import com.crewup.myapplication.ui.screens.PlanLocationScreen
import com.crewup.myapplication.ui.screens.ProfileScreen
import com.crewup.myapplication.ui.screens.RegisterScreen
import com.crewup.myapplication.ui.screens.SecurityScreen
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
            navController.navigate(Routes.Home.route) {
                popUpTo(Routes.Login.route) { inclusive = true }
            }
        } else {
            navController.navigate(Routes.Login.route) {
                popUpTo(Routes.Home.route) { inclusive = true }
            }
        }
    }

    NavHost(navController = navController, startDestination = Routes.Login.route) {
        composable(Routes.Login.route) {
            LoginScreen(
                authState = authState,
                onEmailLogin = { email, password ->
                    authViewModel.signInWithEmailPassword(email, password)
                },
                onEmailRegister = { name, lastName, email, password, country, city, phone ->
                    authViewModel.registerWithEmailPassword(
                        name,
                        lastName,
                        email,
                        password,
                        country,
                        city,
                        phone
                    )
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
                },
                onNavigateToRegister = {
                    navController.navigate(Routes.Register.route)
                }
            )
        }


        composable(Routes.Register.route) {
            RegisterScreen(
                authState = authState,
                onEmailRegister = { name, lastName, email, password, country, city, phone ->
                    authViewModel.registerWithEmailPassword(name, lastName, email, password, country, city, phone)
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
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        // Pantallas principales
        composable(Routes.Home.route) {
            HomeScreen(
                user = authState.user,
                navController = navController
            )
        }

        composable(Routes.Profile.route) {
            ProfileScreen(
                navController = navController,
                onSignOut = { authViewModel.signOut() }
            )
        }

        // Pantallas de configuración
        composable(Routes.EditProfile.route) {
            EditProfileScreen(navController = navController)
        }

        composable(Routes.Security.route) {
            SecurityScreen(navController = navController)
        }

        composable(Routes.Notifications.route) {
            NotificationsScreen(navController = navController)
        }

        composable(Routes.PlanLocation.route) {
            PlanLocationScreen(navController = navController)
        }

        composable(Routes.PlanDate.route) {
            PlanDateScreen(navController = navController)
        }

        // Ruta de creación de plan
        composable(Routes.CreatePlan.route) {
            CreatePlanFlowScreen(navController = navController)
        }
    }
}
