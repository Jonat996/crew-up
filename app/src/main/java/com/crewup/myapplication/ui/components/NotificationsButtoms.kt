package com.example.qta_jw_3.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen() {
    var generalNotification by remember { mutableStateOf(true) }
    var sound by remember { mutableStateOf(false) }
    var vibrate by remember { mutableStateOf(true) }

    var appUpdates by remember { mutableStateOf(false) }
    var planReminder by remember { mutableStateOf(true) }
    var attendanceConfirmation by remember { mutableStateOf(false) }
    var planChange by remember { mutableStateOf(false) }

    var newGroup by remember { mutableStateOf(false) }
    var planSuggestions by remember { mutableStateOf(true) }

    val lightBlue = Color(0xFF64B5F6)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        SectionTitle("Común")
        NotificationSwitch("Notificación general", generalNotification, lightBlue) { generalNotification = it }
        NotificationSwitch("Sonido", sound, lightBlue) { sound = it }
        NotificationSwitch("Vibrar", vibrate, lightBlue) { vibrate = it }

        Divider(Modifier.padding(vertical = 8.dp))

        SectionTitle("Actualización de sistema y servicios")
        NotificationSwitch("Actualizaciones de la aplicación", appUpdates, lightBlue) { appUpdates = it }
        NotificationSwitch("Recordatorio de plan", planReminder, lightBlue) { planReminder = it }
        NotificationSwitch("Confirmación de asistencia", attendanceConfirmation, lightBlue) { attendanceConfirmation = it }
        NotificationSwitch("Cambio en el plan", planChange, lightBlue) { planChange = it }

        Divider(Modifier.padding(vertical = 8.dp))

        SectionTitle("Otros")
        NotificationSwitch("Nuevo grupo creado", newGroup, lightBlue) { newGroup = it }
        NotificationSwitch("Sugerencias de planes", planSuggestions, lightBlue) { planSuggestions = it }
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
fun NotificationSwitch(
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
fun NotificationsScreenPreview() {
    MaterialTheme {
        NotificationsScreen()
    }
}
