import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// Reemplaza con tu R.drawable.logo_pizza real
// import com.ejemplo.miapp.R
// En este ejemplo, usaré un recurso ficticio para la demostración
// Solo funciona si ya has importado tus recursos.
// Asume que R.drawable.logo_pizza existe.

@Composable
fun Header(
    title: String,
    // Puedes pasar un ID de recurso para el logo si lo quieres hacer flexible
    logoResId: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .background(Color(0xFF0056B3)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            image
            // Usamos un simple texto si no tienes el recurso de imagen
            Text(
                text = "🍕", // Placeholder para el logo si no tienes la imagen
                fontSize = 80.sp,
                color = Color.White,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = title,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Preview
fun HeaderPreview (

)