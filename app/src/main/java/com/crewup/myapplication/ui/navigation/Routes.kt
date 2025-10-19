package com.crewup.myapplication.ui.navigation

/**
 * Define todas las rutas de navegación de la aplicación
 */
sealed class Routes(val route: String) {
    // Rutas de autenticación
    data object Login : Routes("login")
    data object Register : Routes("register")

    // Rutas principales
    data object Home : Routes("home")
    data object Profile : Routes("profile")

    // Rutas de configuración de perfil
    data object EditProfile : Routes("edit_profile")
    data object Security : Routes("security")
    data object Notifications : Routes("notifications")
    data object Privacy : Routes("privacy")

    // Rutas adicionales
    data object Help : Routes("help")
    data object ReportProblem : Routes("report_problem")
    data object MyPlans : Routes("my_plans")
    data object TermsAndPolicies : Routes("terms_policies")
}
