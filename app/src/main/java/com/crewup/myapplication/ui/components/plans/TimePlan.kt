package com.crewup.myapplication.ui.components.plans

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalTime

// Clase para manejar el estado AM/PM
enum class AmPm { AM, PM }

@Composable
fun TimePlan(
    selectedTime: LocalTime,
    onTimeSelected: (LocalTime) -> Unit,
    is24Hour: Boolean = false,
    modifier: Modifier = Modifier
) {
    // 1. Estados iniciales
    var hour by remember { mutableIntStateOf(selectedTime.hour) }
    var minute by remember { mutableIntStateOf(selectedTime.minute) }
    // Omitimos segundos como pediste, pero el patrón sería el mismo
    var amPm by remember { mutableStateOf(if (selectedTime.hour >= 12) AmPm.PM else AmPm.AM) }

    // 2. Efecto para actualizar el LocalTime cuando los estados internos cambian
    LaunchedEffect(hour, minute, amPm) {
        val finalHour = when {
            is24Hour -> hour
            amPm == AmPm.PM && hour < 12 -> hour + 12
            amPm == AmPm.AM && hour == 12 -> 0 // 12 AM (medianoche) es 0 en 24h
            else -> hour
        }

        onTimeSelected(LocalTime.of(finalHour.coerceIn(0, 23), minute.coerceIn(0, 59)))
    }

    // Convertir a formato 12h para la UI (ej: 14h -> 02h)
    val displayHour = if (is24Hour) hour else {
        when (hour) {
            0, 12 -> 12 // 00:xx y 12:xx son 12:xx en 12h
            in 13..23 -> hour - 12
            else -> hour
        }
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // --- COLUMNA HORA ---
        TimeSegment(
            value = displayHour,
            onValueChange = { newH -> hour = newH },
            label = "Horas",
            range = if (is24Hour) 0..23 else 1..12,
            twoDigit = true
        )

        // Separador de dos puntos
        Text(":", fontSize = 36.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp), color = MaterialTheme.colorScheme.primary)

        // --- COLUMNA MINUTO ---
        TimeSegment(
            value = minute,
            onValueChange = { minute = it },
            label = "Minutos",
            range = 0..59,
            twoDigit = true
        )

        // Separador de dos puntos (Omitido ya que quitamos los segundos)
        // Si quisieras los segundos, iría aquí, seguido de la Columna Segundos.

        // --- COLUMNA AM/PM ---
        if (!is24Hour) {
            Spacer(Modifier.width(20.dp))
            AmPmSegment(
                amPm = amPm,
                onAmPmChange = { amPm = it },
                modifier = Modifier.height(140.dp) // Ajustar altura para alineación
            )
        }
    }
}

// Componente para un segmento de hora/minuto (Simulando el spinner)
@Composable
fun TimeSegment(
    value: Int,
    onValueChange: (Int) -> Unit,
    label: String,
    range: IntRange,
    twoDigit: Boolean
) {
    // ⚠️ NOTA: Esto es una simulación visual. Un spinner real usaría LazyColumn o una librería de terceros.
    // Aquí solo mostramos el valor central y los valores anteriores/siguientes para imitar el diseño.

    val displayValue = if (twoDigit) String.format("%02d", value) else value.toString()
    val prevValue = if (value - 1 < range.first) range.last else value - 1
    val nextValue = if (value + 1 > range.last) range.first else value + 1

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Valor superior (desplazado)
        Text(
            text = if (twoDigit) String.format("%02d", prevValue) else prevValue.toString(),
            fontSize = 30.sp,
            color = Color.Gray,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        // Valor central (seleccionado)
        Text(
            text = displayValue,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0056B3),
            modifier = Modifier
                .padding(vertical = 4.dp)
                .background(Color.Transparent) // Fondo transparente para enfatizar el texto
                .clickable { /* Abre un diálogo o dispara la acción de cambio */ }
        )

        // Valor inferior (desplazado)
        Text(
            text = if (twoDigit) String.format("%02d", nextValue) else nextValue.toString(),
            fontSize = 30.sp,
            color = Color.Black,
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }
}

// Componente para el selector AM/PM
@Composable
fun AmPmSegment(amPm: AmPm, onAmPmChange: (AmPm) -> Unit, modifier: Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = AmPm.AM.name,
            fontSize = 30.sp,
            color = if (amPm == AmPm.AM) MaterialTheme.colorScheme.primary else Color.Gray,
            modifier = Modifier
                .padding(vertical = 4.dp)
                .clickable { onAmPmChange(AmPm.AM) }
        )
        Spacer(Modifier.height(10.dp)) // Espaciador central
        Text(
            text = AmPm.PM.name,
            fontSize = 30.sp,
            color = if (amPm == AmPm.PM) Color(0xFF0056B3) else Color.Gray,
            modifier = Modifier
                .padding(vertical = 4.dp)
                .clickable { onAmPmChange(AmPm.PM) }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TimePlanSpinnerPreview() {
    MaterialTheme {
        TimePlan(
            selectedTime = LocalTime.of(6, 28), // 06:28 PM
            onTimeSelected = {},
            is24Hour = false,
            modifier = Modifier.padding(vertical = 20.dp)
        )
    }
}