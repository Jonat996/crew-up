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
    object PlanLocation: Routes("plans")
    object PlanDate : Routes("plan_date")

    // Ruta de creación de plan
    data object CreatePlan : Routes("create_plan")
    // Ruta de edición de plan (con parámetro planId)
    data object EditPlan : Routes("edit_plan/{planId}") {
        fun createRoute(planId: String) = "edit_plan/$planId"
    }
    // Ruta de chat grupal (con parámetro planId)
    data object GroupChat : Routes("group_chat/{planId}") {
        fun createRoute(planId: String) = "group_chat/$planId"
    }
    // Rutas de recuperación de contraseña
    data object RecoverEmail : Routes("recover_email")
    data object CodeSent : Routes("code_sent")
    data object OtpVerification : Routes("otp_verification")
    data object ChangePassword : Routes("change_password")
    data object NotFound : Routes("not_found")
}
