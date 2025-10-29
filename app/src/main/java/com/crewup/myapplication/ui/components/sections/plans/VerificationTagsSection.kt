package com.crewup.myapplication.ui.components.sections.plans

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun VerificationTagsSection(
    tags: List<String>,
    modifier: Modifier = Modifier
) {

    Column(modifier = modifier.padding(vertical = 8.dp)) {
        if (tags.isEmpty()) {
            Text(
                text = "Sin categorías",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 16.dp).background(color = Color(0xFF0056B3))
            )
        } else {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                tags.forEach { tag ->
                    AssistChip(
                        onClick = {},
                        label = { Text(tag) },
                        enabled = true,
                        colors = AssistChipDefaults.assistChipColors(
                            labelColor = Color.White,
                            containerColor = Color(0xFF0056B3).copy(alpha = 0.7f),

                    )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewVerificationTagsStyled() {
    MaterialTheme {
        VerificationTagsSection(
            tags = listOf("Intercambio Idiomas", "Cantar", "Juegos de Mesa"),
            modifier = Modifier.padding(16.dp)
        )
    }
}