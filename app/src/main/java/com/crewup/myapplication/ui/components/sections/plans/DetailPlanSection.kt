package com.crewup.myapplication.ui.components.sections.plans

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crewup.myapplication.R
import com.crewup.myapplication.models.DetailPlan
import com.crewup.myapplication.models.mockPlanDetail

@Composable
fun DetailPlanSection(plan: DetailPlan) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F0F0))
    ) {
    }
    Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(0.dp) // Sin borde redondeado en la parte superior
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp,end=16.dp)
                    .height(180.dp),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_profile),
                    contentDescription = "Simulacion"
                )
            }

            // --- 2. CONTENIDO PRINCIPAL (Dentro de un Card blanco con sombra) ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    // TÍTULO y UBICACIÓN
                    Text(
                        text = plan.title,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 32.sp,
                        color = Color.Black
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = plan.locationName,
                        fontSize = 16.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Divider(color = Color.LightGray, thickness = 1.dp)
                    Spacer(Modifier.height(16.dp))

                    // TAGS (Etiquetas de categoría)

                    Spacer(Modifier.height(24.dp))

                    // FECHA Y HORA
                    Text(
                        text = plan.date,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = plan.time,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                    Spacer(Modifier.height(24.dp))

                    // BOTÓN DE MODIFICACIÓN
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            onClick = { },

                            modifier = Modifier
                                .widthIn(min = 150.dp, max = 300.dp)
                                .height(50.dp)
                                .padding(vertical = 4.dp),

                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF0056B3),
                                contentColor = Color.White,
                            ),

                            shape = RoundedCornerShape(10.dp),

                            // 3. APLICAR PADDING INTERNO para centrar el texto verticalmente
                            contentPadding = PaddingValues(vertical = 12.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.modifier_plan),
                                fontWeight = FontWeight.SemiBold,

                                )
                        }
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {  },
                modifier = Modifier
                    .widthIn(min = 140.dp, max = 200.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0056B3),
                    contentColor = Color.White,
                ),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                Text(
                    text = stringResource(R.string.create_plan),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
       }
     }


    // --- Preview de la Pantalla ---
    @Preview(showBackground = true)
    @Composable
    fun PlanDetailScreenPreview() {
        MaterialTheme {
            DetailPlanSection(plan = mockPlanDetail)
        }
    }