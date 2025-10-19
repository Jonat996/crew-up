package com.crewup.myapplication.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.crewup.myapplication.ui.navigation.Routes
import androidx.compose.ui.res.stringResource
import com.crewup.myapplication.R

data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

@Composable
fun BottomNavBar(navController: NavController) {
    val homeLabel = stringResource(R.string.home)
    val profileLabel = stringResource(R.string.profile)

    val items = listOf(
        BottomNavItem(Routes.Home.route, Icons.Default.Home, homeLabel),
        BottomNavItem(Routes.Profile.route, Icons.Default.Person, profileLabel)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            // Evitar múltiples copias de la misma pantalla en el back stack
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            // Evitar múltiples copias del mismo destino
                            launchSingleTop = true
                            // Restaurar el estado cuando se vuelva a seleccionar
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}
