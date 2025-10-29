package com.crewup.myapplication.ui.components.sections.plans

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crewup.myapplication.models.Plan
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.compose.foundation.layout.*
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest


@Composable
fun VerificationItemStyled(
    label: String,
    value: String,
    onEdit: () -> Unit = {},
    showDivider: Boolean = true
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Columna de Etiqueta y Valor
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = value,
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Botón/Texto Modificar
            TextButton(
                onClick = onEdit,
                modifier = Modifier.height(30.dp),
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                Text(
                    text = "Modificar",
                    color = Color(0xFF0056B3),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (showDivider) {
                Divider(color = Color.LightGray.copy(alpha = 0.5f), thickness = 1.dp)
        }
    }
}
@Composable
fun VerificationSection(
    plan: Plan,
    onModify: (Int) -> Unit
) {

    // Contenedor principal con fondo claro
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F0F0))
    ) {

        // Tarjeta de Contenido Principal (Estilo de tarjeta)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {

            // Mostrar la imagen cargada desde Firebase
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(180.dp)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(plan.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Imagen del plan",
                    modifier = Modifier.fillMaxSize()
                )
            }

            Column(modifier = Modifier.padding(20.dp)) {

                // TÍTULO DEL PLAN
                Text(
                    text = plan.title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 32.sp,
                    color = Color.Black
                )
                Spacer(Modifier.height(4.dp))

                // UBICACIÓN
                Text(
                    text = plan.location.name.ifBlank { "Ubicación no especificada" },
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Spacer(Modifier.height(5.dp))
                //DESCRIPCION
                Text(
                    text = plan.description.ifBlank { "Sin descripción" },
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 12.dp)

                )

                Divider(color = Color.LightGray.copy(alpha = 0.5f), thickness = 1.dp)

                // 3. Categorías
                VerificationTagsSection(
                    tags = plan.tags
                )
                // 4. Hora

                Text(
                    text = plan.time.ifBlank { "No especificada"},
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // 5. Fecha
                Text(
                    text = plan.date?.let {
                        SimpleDateFormat("dd MMM yyyy", Locale("es")).format(it.toDate())
                    } ?: "No especificada",
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

            }
        }

        Spacer(Modifier.height(32.dp))

    }
}