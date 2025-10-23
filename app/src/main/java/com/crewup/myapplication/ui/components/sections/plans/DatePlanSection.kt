package com.crewup.myapplication.ui.components.sections.plans

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.crewup.myapplication.ui.components.plans.DatePlan
import com.crewup.myapplication.ui.components.plans.TimePlan
import com.crewup.myapplication.viewmodel.PlanDateViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePlanSection() {
    val viewModel: PlanDateViewModel = viewModel()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val selectedTime by viewModel.selectedTime.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {

        Spacer(modifier = Modifier.height(8.dp))
        DatePlan(
            selectedDate = selectedDate,
            onDateSelected = { newDate ->
                viewModel.updateSelectedDate(newDate)
            }
        )

        Spacer(modifier = Modifier.height(18.dp))

        TimePlan(
            selectedTime = selectedTime,
            onTimeSelected = { newTime ->
                viewModel.updateSelectedTime(newTime)
            },
            is24Hour = false
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewDatePlanSection() {
    DatePlanSection()

}