package com.crewup.myapplication.ui.components.header

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crewup.myapplication.R

@Composable
fun HeaderLogo(title: String) {
    HeaderBase {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 5.dp),
            contentAlignment = Alignment.Center
        ) {
            // Logo en el centro pero más arriba
            Image(
                painter = painterResource(id = R.drawable.ic_pizza_logo),
                contentDescription = "Logo CrewUp",
                modifier = Modifier
                    .size(160.dp)
                    .offset(y = (-20).dp)
            )

            // Texto superpuesto en la parte inferior
            Text(
                text = title,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewHeaderLogo() {
    HeaderLogo(title = "CrewUp")
}
