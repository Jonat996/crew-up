package com.crewup.myapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 1. Clase de datos para representar cada opción
data class ProfileOption(
    val icon: ImageVector,
    val text: String,
    val badgeCount: Int? = null, // Opcional, para el número de notificaciones
    val onClick: () -> Unit
)

@Composable
fun ProfileOptionsList(
    options: List<ProfileOption>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp), // Padding a los lados de la Card
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // Sombra
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            options.forEachIndexed { index, option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = option.onClick) // Hace toda la fila clickeable
                        .padding(vertical = 12.dp, horizontal = 16.dp), // Padding interno de cada opción
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = option.icon,
                        contentDescription = option.text,
                        tint = Color(0xFF0056B3), // Color del icono (azul)
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(16.dp))

                    Text(
                        text = option.text,
                        fontSize = 16.sp,
                        color = Color.Black,
                        modifier = Modifier.weight(1f) // Ocupa el espacio restante
                    )


                }
            }
        }
    }
}

// --- Preview del Componente ---

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileOptionsListPreview() {
    val sampleOptions = listOf(
        ProfileOption(
            icon = Icons.Default.Person,
            text = "Editar perfil",
            onClick = { /* Navegar a editar perfil */ }
        ),
        ProfileOption(
            icon = Icons.Default.Security,
            text = "Seguridad",
            onClick = { /* Navegar a seguridad */ }
        ),
        ProfileOption(
            icon = Icons.Default.Notifications,
            text = "Notificaciones",
            badgeCount = 9, // Con un badge
            onClick = { /* Navegar a notificaciones */ }
        ),
        ProfileOption(
            icon = Icons.Default.Lock,
            text = "Privacidad",
            onClick = { /* Navegar a privacidad */ }
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray.copy(alpha = 0.3f)) // Un fondo para que se vea la Card
            .padding(top = 20.dp), // Espacio arriba
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título "Cuenta"
        Text(
            text = "Cuenta",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp, start = 16.dp)
        )
        ProfileOptionsList(options = sampleOptions)
    }
}