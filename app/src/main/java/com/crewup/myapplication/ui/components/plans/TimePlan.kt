package com.crewup.myapplication.ui.components.plans

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
            label = "",
            range = if (is24Hour) 0..23 else 1..12,
            twoDigit = true
        )

        // Separador de dos puntos
        Text(
            text = ":",
            fontSize = 36.sp, fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(horizontal = 4.dp),
            color =Color(0xFF0056B3)
        )

        // --- COLUMNA MINUTO ---
        TimeSegment(
            value = minute,
            onValueChange = { minute = it },
            label = "",
            range = 0..59,
            twoDigit = true
        )

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
    val listState = rememberLazyListState()
    val displayValues = range.toList()
    var hasInitialized by remember { mutableStateOf(false) }

    // Centrar solo al inicio
    LaunchedEffect(Unit) {
        val targetIndex = displayValues.indexOf(value.coerceIn(range))
        listState.scrollToItem(targetIndex)
        hasInitialized = true
    }

    // Actualizar el valor solo cuando el usuario hace scroll
    LaunchedEffect(
        listState.firstVisibleItemIndex,
        listState.layoutInfo.visibleItemsInfo,
        hasInitialized
    ) {
        if (hasInitialized) {
            val visibleItems = listState.layoutInfo.visibleItemsInfo
            if (visibleItems.isNotEmpty()) {
                val centerIndex = listState.firstVisibleItemIndex + visibleItems.size / 2
                val adjustedIndex = centerIndex.coerceIn(displayValues.indices)
                val newValue = displayValues[adjustedIndex]
                if (newValue != value) {
                    onValueChange(newValue)
                }
            }
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn(
            state = listState,
            modifier = Modifier
                .background(Color.LightGray.copy(alpha = 0.1f))
                .padding(vertical = 16.dp)
                .height(120.dp)
        ) {
            items(displayValues) { item ->
                val isSelected =
                    displayValues.indexOf(item) == (listState.firstVisibleItemIndex + listState.layoutInfo.visibleItemsInfo.size / 2).coerceIn(
                        displayValues.indices
                    )
                Text(
                    text = if (twoDigit) String.format("%02d", item) else item.toString(),
                    fontSize = if (isSelected) 36.sp else 30.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) Color(0xFF0056B3) else Color.Gray,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}
// Componente para el selector AM/PM
@Composable
fun AmPmSegment(
    amPm: AmPm,
    onAmPmChange: (AmPm) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val displayValues = listOf(AmPm.AM, AmPm.PM)
    var hasInitialized by remember { mutableStateOf(false) }

    // Centrar solo al inicio
    LaunchedEffect(Unit) {
        val targetIndex = displayValues.indexOf(amPm)
        listState.scrollToItem(targetIndex)
        hasInitialized = true
    }

    // Actualizar el valor solo cuando el usuario hace scroll
    LaunchedEffect(listState.firstVisibleItemIndex, listState.layoutInfo.visibleItemsInfo, hasInitialized) {
        if (hasInitialized) {
            val visibleItems = listState.layoutInfo.visibleItemsInfo
            if (visibleItems.isNotEmpty()) {
                val centerIndex = listState.firstVisibleItemIndex + visibleItems.size / 2
                val adjustedIndex = centerIndex.coerceIn(displayValues.indices)
                val newValue = displayValues[adjustedIndex]
                if (newValue != amPm) {
                    onAmPmChange(newValue)
                }
            }
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .background(Color.LightGray.copy(alpha = 0.1f))
                .padding(vertical = 26.dp)
                .height(100.dp)
        ) {
            items(displayValues) { item ->
                val isSelected = displayValues.indexOf(item) == (listState.firstVisibleItemIndex + listState.layoutInfo.visibleItemsInfo.size / 2).coerceIn(displayValues.indices)
                Text(
                    text = item.name,
                    fontSize = if (isSelected) 30.sp else 24.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) Color(0xFF0056B3) else Color.Gray,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
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