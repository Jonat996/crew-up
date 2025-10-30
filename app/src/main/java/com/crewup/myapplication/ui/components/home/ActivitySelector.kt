package com.crewup.myapplication.ui.components.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ActivityOption(
    val label: String,
    val iconRes: Int,
    val onClick: () -> Unit
)

@Composable
fun ActivitySelector(
    activities: List<ActivityOption>,
    selectedActivity: String?,
    onActivitySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        activities.forEach { activity ->
            val isSelected = selectedActivity == activity.label
            val animatedColor by animateColorAsState(
                targetValue = if (isSelected) Color(0xFF0056B3) else Color.Gray,
                animationSpec = tween(durationMillis = 300)
            )

            // 🔹 Cambio principal: el clic ahora selecciona y ejecuta onClick
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable {
                        onActivitySelected(activity.label)
                        activity.onClick() // 👉 Ejecuta la acción (por ejemplo, navegar)
                    }
                    .padding(vertical = 8.dp)
            ) {
                Icon(
                    painter = painterResource(id = activity.iconRes),
                    contentDescription = activity.label,
                    tint = animatedColor,
                    modifier = Modifier.size(32.dp)
                )
                Text(
                    text = activity.label,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = animatedColor
                )
                AnimatedVisibility(visible = isSelected) {
                    Box(
                        modifier = Modifier
                            .height(2.dp)
                            .width(40.dp)
                            .background(Color(0xFF0056B3))
                    )
                }
            }
        }
    }
}
