package com.crewup.myapplication.ui.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crewup.myapplication.R

@Composable
fun SearchHeaderBar(
    modifier: Modifier = Modifier,
    query: String = "",
    onSearchClick: () -> Unit = {},
    onFilterClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .width(360.dp)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .background(Color.White, shape = RoundedCornerShape(50))
            .height(68.dp)
    ) {
        Row(
            // Este Row contiene todos los elementos de la barra
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
           horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // 1. ÍCONO DE BÚSQUEDA
            Icon(
                painter = painterResource(R.drawable.ic_home_search),
                contentDescription = "Buscar",
                tint = Color.Black,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onSearchClick() }
            )

            // 2. TEXTO (ocupa el espacio central)
            Text(
                text = query.ifBlank { "¿Qué quieres hacer hoy?" },
                fontSize = 12.sp,
                color = Color.Black,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            )

            Icon(
                painter = painterResource(id = R.drawable.ic_home_filter),
                contentDescription = "Filtro",
                modifier = Modifier
                    .size(28.dp)
                    .clickable { onProfileClick() }
            )


            Spacer(modifier = Modifier.width(12.dp))

            Icon(
                painter = painterResource(id = R.drawable.ic_home_profile),
                contentDescription = "Perfil",
                tint = Color(0xFF0056B3),
                modifier = Modifier
                    .size(28.dp)
                    .clickable { onProfileClick() }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewSearchHeaderBar() {
    var query by remember { mutableStateOf("") }

    SearchHeaderBar(
        query = query,
        onSearchClick = { /* acción simulada */ },
        onFilterClick = {},
        onProfileClick = { /* acción simulada */ }
    )
}

