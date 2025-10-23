package com.crewup.myapplication.ui.components.sections.plans

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePlanSection() {
    val viewModel: PlanDateViewModel = viewModel()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val selectedTime by viewModel.selectedTime.collectAsState()

    Column(modifier = Modifier
        .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        Spacer(modifier = Modifier.height(18.dp).padding(20.dp))
        Text(
            text = stringResource(R.string.select_the_day),
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp

        )
       Spacer(modifier = Modifier.height(8.dp))
        DatePlan(
            selectedDate = selectedDate,
            onDateSelected = { newDate ->
                viewModel.updateSelectedDate(newDate)
            }
        )

        Spacer(modifier = Modifier.height(18.dp))
        Text(
            text = stringResource(R.string.what_time),
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp

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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {  },
                modifier = Modifier
                    .widthIn(min = 140.dp, max = 200.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0056B3),
                    contentColor = Color.White,
                ),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                Text(
                    text = stringResource(R.string.button_plan),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewDatePlanSection() {
    DatePlanSection()

}