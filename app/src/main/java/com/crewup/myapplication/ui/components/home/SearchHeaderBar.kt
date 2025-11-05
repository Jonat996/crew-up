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
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
    query: String,
    onQueryChange: (String) -> Unit,
    onSearchClick: () -> Unit = {},
    onFilterClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Input de búsqueda con íconos internos
        TextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text("¿Qué quieres hacer hoy?", fontSize = 14.sp) },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color.Black
            ),
            shape = RoundedCornerShape(50),

            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_home_search),
                    contentDescription = "Buscar",
                    tint = Color.Black,
                    modifier = Modifier.clickable { onSearchClick() }
                )
            },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_home_filter),
                    contentDescription = "Filtro",
                    modifier = Modifier.clickable { onFilterClick() }
                )
            },
            modifier = Modifier
                .weight(1f)
                .height(52.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))
        // Ícono de perfil fuera del input
        Icon(
            painter = painterResource(id = R.drawable.ic_home_profile),
            contentDescription = "Perfil",
            tint = Color(0xFF0056B3),
            modifier = Modifier
                .size(32.dp)
                .clickable { onProfileClick() }
        )



    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewSearchHeaderBar() {
    var query by remember { mutableStateOf("") }

    SearchHeaderBar(
        query = query,
        onQueryChange = {},
        onSearchClick = { /* acción simulada */ },
        onFilterClick = {},
        onProfileClick = { /* acción simulada */ }
    )
}

