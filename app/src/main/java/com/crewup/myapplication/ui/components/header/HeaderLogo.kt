package com.crewup.myapplication.ui.components.header

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crewup.myapplication.R

@Composable
fun HeaderLogo(title: String) {
    HeaderBase {
        Image(
            painter = painterResource(id = R.drawable.ic_pizza_logo),
            contentDescription = "Logo CrewUp",
            modifier = Modifier
                .size(120.dp)
                .border(width = 2.dp, color = Color.White, shape = CircleShape)
        )
        Text(
            text = title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(top = 4.dp) // Espacio reducido

        )
    }
}

@Preview (showBackground = true)
@Composable
fun previewHeaderLogo(){
    HeaderLogo(title= "Crew Up")
}
