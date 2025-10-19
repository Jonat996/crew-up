package com.example.qta_jw_3.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft

@Composable
fun BackIcon(
    navController: NavController,
    tint: Color = Color.White,
    size: Int = 32
) {
    Icon(
        imageVector = Icons.Default.ChevronLeft,
        contentDescription = "Volver",
        tint = tint,
        modifier = Modifier
            .size(size.dp)
            .clickable { navController.popBackStack() }
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF1976D2)
@Composable
fun BackIconPreview() {
    val navController = rememberNavController()
    BackIcon(navController = navController)
}
