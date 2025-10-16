package com.crewup.myapplication.ui.components.header

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.crewup.myapplication.R

@Composable
fun HeaderBase(content: @Composable ColumnScope.() -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp) // ajusta según tu diseño
    ) {
        // 🔹 Capa 1: fondo azul sólido
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color(0xFF0056B3))
        )

        // 🔹 Capa 2: imagen decorativa con puntos blancos
        Image(
            painter = painterResource(id = R.drawable.dots), // tu imagen de puntos
            contentDescription = null,
            contentScale = ContentScale.Crop, // para cubrir bien el fondo
            modifier = Modifier
                .matchParentSize()
        )

        // 🔹 Capa 3: contenido dinámico
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            content = content
        )
    }
}

@Preview (showBackground = true)
@Composable
fun previewContent(){
    HeaderBase(){

    }
}