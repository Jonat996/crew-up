package com.crewup.myapplication.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun SocialButton(
    text: String,
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        border = ButtonDefaults.outlinedButtonBorder.copy(
        brush = androidx.compose.ui.graphics.SolidColor(Color.White)
        )
    ) {
        icon()
        Spacer(Modifier.width(8.dp))
        Text(text = text, fontWeight = FontWeight.SemiBold)
    }
}



@Preview(showBackground = true, showSystemUi = true,name = "SocialButton Ancho Personalizado")
@Composable
fun SocialButtonPreview() {
    // MiAplicacionTheme {
    SocialButton(
        text = "Google",
        onClick = { /* Acción */ },
        icon = {
            Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = "Google Logo"
            )
        },
        modifier = Modifier.width(200.dp) )
}