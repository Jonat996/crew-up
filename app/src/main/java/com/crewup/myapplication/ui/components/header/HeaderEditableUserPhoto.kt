package com.crewup.myapplication.ui.components.header

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.crewup.myapplication.R
import com.crewup.myapplication.viewmodel.UserViewModel

/**
 * Header con foto de perfil editable.
 * Muestra la foto del usuario en un círculo con borde y un ícono de lápiz para editarla.
 */
@Composable
fun HeaderEditableUserPhoto(
    title: String,
    navController: NavController? = null,
    userViewModel: UserViewModel = viewModel()
) {
    val userState by userViewModel.userState.collectAsState()
    val user = userState.user
    val photoUrl = user?.photoUrl?.takeIf { it.isNotBlank() }
    val isLoading = userState.isLoading

    // Launcher para seleccionar imagen
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { userViewModel.uploadPhoto(it) }
    }

    HeaderBase(navController = navController) {
        Spacer(modifier = Modifier.height(8.dp))

        // Contenedor de la foto de perfil con el ícono de edición
        Box(
            modifier = Modifier.size(110.dp),
            contentAlignment = Alignment.Center
        ) {
            // Foto de perfil en círculo con borde
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .border(3.dp, Color.White, CircleShape)
                    .clip(CircleShape)
                    .background(Color(0xFFE3F2FD)) // Fondo azul claro para contraste con logo blanco
                    .clickable { imagePickerLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (photoUrl != null) {
                    AsyncImage(
                        model = photoUrl,
                        contentDescription = "Foto de perfil",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // Logo por defecto
                    AsyncImage(
                        model = R.drawable.ic_pizza_logo,
                        contentDescription = "Logo por defecto",
                        modifier = Modifier
                            .size(70.dp)
                            .padding(8.dp)
                    )
                }

                // Indicador de carga sobre la imagen
                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }

            // Ícono de lápiz en la esquina inferior derecha
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = (-4).dp, y = (-4).dp)
                    .background(Color(0xFF0056B3), CircleShape)
                    .border(2.dp, Color.White, CircleShape)
                    .clickable { imagePickerLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar foto de perfil",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Título
        Text(
            text = title,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewHeaderEditableUserPhoto() {
    HeaderEditableUserPhoto(title = "Juan Monroy")
}
