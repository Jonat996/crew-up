package com.crewup.myapplication.ui.components.plans

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.foundation.gestures.snapping.SnapLayoutInfoProvider
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior

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
            range = 0..59,
            twoDigit = true
        )

        // --- COLUMNA AM/PM ---
        if (!is24Hour) {
            Spacer(Modifier.width(20.dp))
            AmPmSegment(
                amPm = amPm,
                onAmPmChange = { amPm = it },
                modifier = Modifier.height(180.dp) // Ajustar altura para alineación
            )
        }
    }
}


// Componente para un segmento de hora/minuto
@Composable
fun TimeSegment(
    value: Int,
    onValueChange: (Int) -> Unit,
    range: IntRange,
    twoDigit: Boolean = true
) {
    val listState = rememberLazyListState()
    val displayValues = range.toList()
    val snapLayoutInfoProvider = remember(listState) { SnapLayoutInfoProvider(listState) }
    val flingBehavior = rememberSnapFlingBehavior(snapLayoutInfoProvider)

    // Centrar en el valor inicial
    LaunchedEffect(Unit) {
        val index = displayValues.indexOf(value.coerceIn(range))
        if (index >= 0) listState.scrollToItem(index)
    }

    // Detectar el ítem centrado visualmente
    LaunchedEffect(listState.firstVisibleItemIndex, listState.layoutInfo) {
        val visibleItems = listState.layoutInfo.visibleItemsInfo
        if (visibleItems.isNotEmpty()) {
            val centerY = listState.layoutInfo.viewportEndOffset / 2
            val centeredItem = visibleItems.minByOrNull { item ->
                kotlin.math.abs(item.offset + item.size / 2 - centerY)
            }
            centeredItem?.index?.let { centeredIndex ->
                val newValue = displayValues.getOrNull(centeredIndex)
                if (newValue != null && newValue != value) {
                    onValueChange(newValue)
                }
            }
        }
    }

    Box(
        modifier = Modifier.height(120.dp),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            contentPadding = PaddingValues(vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(displayValues.size) { index ->
                val item = displayValues[index]
                val isSelected = item == value

                Text(
                    text = if (twoDigit) "%02d".format(item) else item.toString(),
                    fontSize = if (isSelected) 36.sp else 28.sp,
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
    Column(
        modifier = modifier
            .height(120.dp)
            .width(70.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AmPm.values().forEach { item ->
            val isSelected = item == amPm
            Text(
                text = item.name,
                fontSize = if (isSelected) 30.sp else 24.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) Color(0xFF0056B3) else Color.Gray,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .clickable {
                        if (!isSelected) onAmPmChange(item)
                    }
            )
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