package com.example.qta_jw_3.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    var passwordLock by remember { mutableStateOf(true) }
    var autoLogout by remember { mutableStateOf(false) }
    var notifyChanges by remember { mutableStateOf(true) }

    var alertNewDevice by remember { mutableStateOf(false) }
    var alertFailedLogin by remember { mutableStateOf(true) }
    var confirmEmailChange by remember { mutableStateOf(false) }

    var suspiciousActivity by remember { mutableStateOf(false) }
    var twoFactorAuth by remember { mutableStateOf(true) }

    val lightBlue = Color(0xFF64B5F6) // Azul clarito (puedes ajustar el tono)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        SectionTitle("General")
        SettingSwitch("Bloqueo con contraseña", passwordLock, lightBlue) { passwordLock = it }
        SettingSwitch("Cerrar sesión automáticamente después de inactividad", autoLogout, lightBlue) { autoLogout = it }
        SettingSwitch("Notificar cambios en la configuración de la cuenta", notifyChanges, lightBlue) { notifyChanges = it }

        Divider(Modifier.padding(vertical = 8.dp))

        SectionTitle("Accesos")
        SettingSwitch("Alertar inicio de sesión en nuevo dispositivo", alertNewDevice, lightBlue) { alertNewDevice = it }
        SettingSwitch("Alertar intento de inicio fallido", alertFailedLogin, lightBlue) { alertFailedLogin = it }
        SettingSwitch("Confirmación por correo en cambios de contraseña", confirmEmailChange, lightBlue) { confirmEmailChange = it }

        Divider(Modifier.padding(vertical = 8.dp))

        SectionTitle("Otros")
        SettingSwitch("Actividad sospechosa detectada", suspiciousActivity, lightBlue) { suspiciousActivity = it }
        SettingSwitch("Verificación en dos pasos", twoFactorAuth, lightBlue) { twoFactorAuth = it }
    }
}


@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        modifier = Modifier.padding(bottom = 4.dp)
    )
}


@Composable
fun SettingSwitch(
    title: String,
    checked: Boolean,
    color: Color,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = color,
                checkedTrackColor = color.copy(alpha = 0.4f),
                uncheckedThumbColor = Color.LightGray,
                uncheckedTrackColor = Color.Gray.copy(alpha = 0.3f)
            )
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun SettingsScreenPreview() {
    MaterialTheme {
        SettingsScreen()
    }
}
