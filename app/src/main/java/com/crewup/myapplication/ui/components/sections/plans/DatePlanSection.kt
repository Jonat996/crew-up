package com.crewup.myapplication.ui.components.sections.plans

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.crewup.myapplication.R
import com.crewup.myapplication.ui.components.plans.DatePlan
import com.crewup.myapplication.ui.components.plans.TimePlan
import com.crewup.myapplication.viewmodel.PlanDateViewModel
import java.time.LocalDateTime
import android.widget.Toast


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePlanSection(
    selectedDate: java.time.LocalDate? = null,
    selectedTime: java.time.LocalTime? = null,
    onDateSelected: (java.time.LocalDate) -> Unit = {},
    onTimeSelected: (java.time.LocalTime) -> Unit = {},
    viewModel: PlanDateViewModel? = null
) {
    val vm: PlanDateViewModel = viewModel ?: viewModel()
    val context = LocalContext.current

    // Estado local para manejar las selecciones
    var localDate by remember(selectedDate) { mutableStateOf(selectedDate ?: java.time.LocalDate.now()) }
    var localTime by remember(selectedTime) { mutableStateOf(selectedTime ?: java.time.LocalTime.of(12, 0)) }

    Column(modifier = Modifier
        .padding(horizontal = 8.dp, vertical = 16.dp)
        .background(color = Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        )
    {
        Spacer(modifier = Modifier.height(15.dp).padding(1.dp))
        Text(
            text = stringResource(R.string.select_the_day),
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp

        )
       Spacer(modifier = Modifier.height(8.dp))
        DatePlan(
            selectedDate = localDate,
            onDateSelected = { newDate ->
                val today = java.time.LocalDate.now()
                if (newDate.isBefore(today)) {
                    Toast.makeText(context, "No puedes seleccionar una fecha anterior a hoy", Toast.LENGTH_SHORT).show()
                } else {
                    localDate = newDate
                    onDateSelected(newDate)
                }
            }
        )

        Spacer(modifier = Modifier.height(18.dp))
        Text(
            text = stringResource(R.string.what_time),
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp

        )
        Spacer(modifier = Modifier.height(18.dp).padding(10.dp))
        TimePlan(
            selectedTime = localTime,
            onTimeSelected = { newTime ->
                val now = LocalDateTime.now()
                val selectedDateTime = LocalDateTime.of(localDate, newTime)

                val isValid = when {
                    localDate.isAfter(now.toLocalDate()) -> true // fecha futura
                    localDate.isEqual(now.toLocalDate()) -> newTime.isAfter(now.toLocalTime()) // hoy, hora futura
                    else -> false // fecha pasada
                }

                if (isValid) {
                    localTime = newTime
                    onTimeSelected(newTime)
                } else {
                    Toast.makeText(context, "La hora seleccionada debe ser posterior a la actual", Toast.LENGTH_SHORT).show()
                }
            },

            is24Hour = false
        )


    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewDatePlanSection() {
    DatePlanSection()

}