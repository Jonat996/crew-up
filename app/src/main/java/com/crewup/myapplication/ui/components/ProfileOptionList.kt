package com.crewup.myapplication.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
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

data class ProfileOption(
    val iconRes: Int,
    val text: String,
    val onClick: () -> Unit
)
@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color.Black,
        modifier = Modifier
        .padding(horizontal = 26.dp, vertical = 8.dp)
    )
}

@Composable
fun ProfileOptionList(
    options: List<ProfileOption>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 26.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            options.forEachIndexed { index, option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = option.onClick)
                        .padding(vertical = 16.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = option.iconRes),
                        contentDescription = option.text,
                        tint = Color(0xFF0056B3),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(16.dp))

                    Text(
                        text = option.text,
                        fontSize = 16.sp,
                        color = Color.Black,
                        modifier = Modifier.weight(1f)
                    )

                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, name = "Profile Options List Preview")
@Composable
fun ProfileOptionListPreview() {

    SectionTitle("Crear cuenta")


    val sampleOptions = listOf(
        ProfileOption(
            iconRes = R.drawable.icon_profile,
            text = "Editar perfil",
            onClick = { /* Acción */ }
        ),
        ProfileOption(
            iconRes = R.drawable.icon_privacy,
            text = "Seguridad",
            onClick = { /* Acción */ }
    )

    )

    Column(modifier = Modifier.padding(vertical = 16.dp)) {
        ProfileOptionList(options = sampleOptions)
    }
}

