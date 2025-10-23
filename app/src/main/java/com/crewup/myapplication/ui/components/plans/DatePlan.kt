package com.crewup.myapplication.ui.components.plans


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.Instant
import java.time.ZoneId
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePlan(
    selectedDate: LocalDate?, // Hacemos selectedDate nullable para cuando no hay selección inicial
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    // Definir el color azul primario
    val CrewUpBlue = Color(0xFF0056B3)

    // Estado del DatePicker
    val datePickerState = rememberDatePickerState(
        // Inicializa con la fecha seleccionada si existe, sino con null para que no haya selección inicial
        initialSelectedDateMillis = selectedDate?.atStartOfDay(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
    )

    // El Card para el contenedor y la sombra
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp), // Ajuste de padding para los lados
        shape = RoundedCornerShape(12.dp), // Bordes redondeados más pronunciados
        colors = CardDefaults.cardColors(containerColor = Color.White), // Fondo blanco
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // Sombra suave como en la imagen
    ) {
        // DatePicker (el calendario)
        DatePicker(
            state = datePickerState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), // Padding interno para el calendario

            // DatePlan.kt (Bloque de colores corregido)

            colors = DatePickerDefaults.colors(
                containerColor = Color.White, // Fondo blanco del calendario

                // Los colores del título/encabezado se mantienen por si se activan en otro momento, aunque están ocultos
                titleContentColor = CrewUpBlue,
                headlineContentColor = CrewUpBlue,

                weekdayContentColor = Color.Gray,

                selectedDayContainerColor = CrewUpBlue, // ⬅️ Fondo del día SELECCIONADO (azul)
                selectedDayContentColor = Color.White,  // ⬅️ Texto del día SELECCIONADO (blanco)

                todayContentColor = CrewUpBlue,     // ⬅️ Color del TEXTO del día actual (azul)
                todayDateBorderColor = CrewUpBlue,  // ⬅️ Borde del día actual (azul)

                dayContentColor = Color.Black // Texto de los días no seleccionados ni actuales
            ),

            title = null,      // Eliminar el título "Selecciona fecha"
            headline = null,   // Eliminar el encabezado (fecha completa)
            showModeToggle = false // Eliminar el botón para cambiar entre vista de calendario y texto
        )
    }

    // Actualiza la fecha seleccionada cuando cambia
    LaunchedEffect(datePickerState.selectedDateMillis) {
        datePickerState.selectedDateMillis?.let { millis ->
            // Convertir milisegundos a LocalDate de forma segura
            val date = Instant.ofEpochMilli(millis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            onDateSelected(date)
        }
    }
}


@Preview(showBackground = true, widthDp = 360, heightDp = 400) // Ajustar tamaño para ver bien el calendario
@Composable
fun DatePlanPreview() {
    MaterialTheme {
        // En el preview, inicializamos con una fecha de ejemplo (hoy) o null
        var selectedDate by remember { mutableStateOf<LocalDate?>(LocalDate.now()) }

        DatePlan(
            selectedDate = selectedDate,
            onDateSelected = { newDate ->
                selectedDate = newDate
                println("Fecha seleccionada en Preview: $newDate")
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}