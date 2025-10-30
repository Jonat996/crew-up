// ui/components/ConfirmationDialog.kt
package com.crewup.myapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.crewup.myapplication.R
import com.crewup.myapplication.models.Plan
import com.crewup.myapplication.models.PlanLocation
import com.crewup.myapplication.models.PlanUser
import com.google.firebase.Timestamp

enum class ConfirmationType {
    JOIN, LEAVE, DELETE
}

// === CARD DE CONFIRMACIÓN (SIN DIÁLOGO) ===
@Composable
fun PlanConfirmationCard(
    plan: Plan,
    type: ConfirmationType,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // TÍTULO + COLOR
            val (title, color) = when (type) {
                ConfirmationType.JOIN -> "¿Estás seguro de unirte al plan?" to Color(0xFF0056B3)
                ConfirmationType.LEAVE -> "¿Estás seguro de salir del plan?" to Color.Red
                ConfirmationType.DELETE -> "¿Estás seguro de eliminar?" to Color.Red
            }

            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )

            Spacer(Modifier.height(12.dp))

            // INFO DEL PLAN
            Text("Tu plan a:", fontSize = 14.sp, color = Color.Gray)
            Spacer(Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color(0xFF0056B3),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = plan.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row {
                Spacer(Modifier.width(20.dp))
                Text(
                    text = plan.location.name.ifBlank { "Ubicación no especificada" },
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Spacer(Modifier.height(12.dp))

            // PARTICIPANTES
            Text("Ya cuenta con:", fontSize = 14.sp, color = Color.Gray)
            Spacer(Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                plan.participants.take(4).forEachIndexed { index, user ->
                    AsyncImage(
                        model = user.photoUrl.ifBlank { null },
                        contentDescription = null,
                        placeholder = painterResource(R.drawable.icon_profile),
                        error = painterResource(R.drawable.icon_profile),
                        modifier = Modifier
                            .size(32.dp)
                            .offset(x = -(index * 12).dp)
                            .clip(CircleShape)
                            .background(Color.LightGray),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "${plan.participants.size} Participantes",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Spacer(Modifier.height(20.dp))

            // BOTONES
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = onDismiss) {
                    Text("Mantener Plan", color = Color(0xFF0056B3))
                }

                Button(
                    onClick = onConfirm,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (type == ConfirmationType.JOIN) Color(0xFF0056B3) else Color.Red
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        when (type) {
                            ConfirmationType.JOIN -> "Unirme"
                            ConfirmationType.LEAVE -> "Salir"
                            ConfirmationType.DELETE -> "Eliminar"
                        },
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

// ================================================
// 3 PREVIEWS: SOLO LA CARD (SIN FONDO)
// ================================================

@Preview(name = "Unirse al plan", showBackground = true)
@Composable
fun JoinConfirmationCardPreview() {
    MaterialTheme {
        PlanConfirmationCard(
            plan = mockPlan,
            type = ConfirmationType.JOIN,
            onConfirm = {},
            onDismiss = {}
        )
    }
}

@Preview(name = "Salir del plan", showBackground = true)
@Composable
fun LeaveConfirmationCardPreview() {
    MaterialTheme {
        PlanConfirmationCard(
            plan = mockPlan,
            type = ConfirmationType.LEAVE,
            onConfirm = {},
            onDismiss = {}
        )
    }
}

@Preview(name = "Eliminar plan", showBackground = true)
@Composable
fun DeleteConfirmationCardPreview() {
    MaterialTheme {
        PlanConfirmationCard(
            plan = mockPlan,
            type = ConfirmationType.DELETE,
            onConfirm = {},
            onDismiss = {}
        )
    }
}

// === MOCK DATA ===
private val mockPlan = Plan(
    id = "plan123",
    title = "Intercambio idiomas y juegos de mesa",
    imageUrl = "https://example.com/plan.jpg",
    location = PlanLocation(name = "Vintrash Bar Bogotá"),
    date = Timestamp.now(),
    time = "7:00 PM",
    tags = listOf("Idiomas", "Juegos"),
    minAge = 18,
    maxAge = 30,
    gender = "Todos",
    createdBy = PlanUser(uid = "creator1", name = "Pedro"),
    participants = listOf(
        PlanUser(uid = "p1", name = "Ana", photoUrl = ""),
        PlanUser(uid = "p2", name = "Luis", photoUrl = ""),
        PlanUser(uid = "p3", name = "María", photoUrl = ""),
        PlanUser(uid = "p4", name = "Carlos", photoUrl = ""),
        PlanUser(uid = "p5", name = "Sofía", photoUrl = "")
    )
)