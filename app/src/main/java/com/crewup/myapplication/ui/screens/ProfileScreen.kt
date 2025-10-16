package com.crewup.myapplication.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.crewup.myapplication.R
import com.crewup.myapplication.ui.components.ProfileOption
import com.crewup.myapplication.ui.components.ProfileOptionList
import com.crewup.myapplication.ui.components.SectionTitle
import com.crewup.myapplication.ui.components.header.HeaderUserInfo
import com.crewup.myapplication.ui.layout.MainLayout

@Composable
fun ProfileScreen(
    navController: NavController,
    onSignOut: () -> Unit
){
    fun getAccountOptions(): List<ProfileOption> {
        return listOf(
            ProfileOption(R.drawable.icon_profile, "Editar perfil", onClick = {}),
            ProfileOption(R.drawable.icon_privacy, "Seguridad", onClick = {}),
            ProfileOption(R.drawable.icon_notification, "Notificaciones", onClick = {}),
            ProfileOption(R.drawable.icon_lock, "Privacidad", onClick = {})
        )
    }

    fun getActionsOptions(): List<ProfileOption> {
        return listOf(
            ProfileOption(R.drawable.icon_report, "Reportar un problema", onClick = {}),
            ProfileOption(R.drawable.icon_plans, "Tus Planes creados", onClick = {}),
            ProfileOption(R.drawable.icon_logout, "Cerrar Sesión", onClick = onSignOut),
        )
    }

    fun getSupportOptions(): List<ProfileOption> {
        return listOf(
            ProfileOption(R.drawable.icon_help, "Ayuda y Soorte", onClick = {}),
            ProfileOption(R.drawable.icon_term, "Términos  y Políticas", onClick = {}),
        )
    }

    MainLayout(
        header = { HeaderUserInfo() },
        content = {
            Column (
                modifier = Modifier
                    .fillMaxSize()
            ) {
                SectionTitle("Cuenta")
                ProfileOptionList(options = getAccountOptions())
                Spacer(Modifier.height(24.dp))
                SectionTitle("Acciones")
                ProfileOptionList(options = getActionsOptions())
                Spacer(Modifier.height(24.dp))
                SectionTitle("Soporte")
                ProfileOptionList(options = getSupportOptions())
            }
        }
    )
}

@Preview(showSystemUi = true, showBackground = true, name = "Full Profile Screen")
@Composable
fun ProfileScreenPreview() {
    MaterialTheme {
        val navController = rememberNavController()
        ProfileScreen(navController = navController, onSignOut = {})
    }
}
