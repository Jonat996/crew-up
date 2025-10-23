package com.crewup.myapplication.ui.components.sections

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crewup.myapplication.R

private val CrewUpBlue = Color(0xFF007BFF)

@Composable
fun NotFoundSection(
    onBackToHome: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "404",
                color = CrewUpBlue,
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold
            )

            Image(
                painter = painterResource(id = R.drawable.ic_pizza_404),
                contentDescription = "Pizza triste",
                modifier = Modifier.size(380.dp) // 🍕 Tamaño grande
            )

            Text(
                text = "Opps!",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = Color.Black
            )

            Text(
                text = "Lo sentimos, la página no se cargó correctamente.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onBackToHome,
                colors = ButtonDefaults.buttonColors(containerColor = CrewUpBlue)
            ) {
                Text("Volver al inicio", color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewNotFoundSection() {
    NotFoundSection()
}
