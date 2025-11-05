package com.crewup.myapplication.ui.components.sections.plans

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crewup.myapplication.R

/**
 * Pantalla inicial del flujo de creación de planes.
 * Muestra un mensaje de bienvenida y anima al usuario a crear su plan.
 */
@Composable
fun CreatePlanSection(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icono o ilustración
        Image(
            painter = painterResource(id = R.drawable.icon_profile), // Cambia por un icono apropiado
            contentDescription = "Crear Plan",
            modifier = Modifier
                .size(120.dp)
                .padding(bottom = 32.dp)
        )

        // Título principal
        Text(
            text = "Crea un Plan",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0056B3),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Descripción
        Text(
            text = "Comparte tus ideas, encuentra personas con intereses similares y haz que sucedan cosas increíbles.",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Lista de beneficios
        BenefitItem(text = "Conecta con personas afines")
        BenefitItem(text = "Organiza actividades fácilmente")
        BenefitItem(text = "Descubre nuevas experiencias")

        Spacer(modifier = Modifier.height(48.dp))

        // Texto motivacional
        Text(
            text = "¡Comencemos!",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF0056B3),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun BenefitItem(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = android.R.drawable.ic_menu_compass),
            contentDescription = null,
            tint = Color(0xFF0056B3),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            fontSize = 16.sp,
            color = Color.Black
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewCreatePlanSection() {
    CreatePlanSection()
}
