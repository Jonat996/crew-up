package com.crewup.myapplication.ui.components.header

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.crewup.myapplication.viewmodel.AuthViewModel

@Composable
fun HeaderUserInfo(
    authViewModel: AuthViewModel = viewModel()
) {
    val authState by authViewModel.authState.collectAsState()
    val user = authState.user

    HeaderBase {
        if (user != null) {
            HeaderUserPhoto(
                title = user.displayName ?: "Sin nombre",
                photoUrl = user.photoUrl?.toString()
            )
            // TODO: Ocupación y localización deben ser obtenidos desde tu base de datos de perfil de usuario
            Text(text = "Ocupación", color = Color.White.copy(alpha = 0.8f))
            Text(text = "Localización", color = Color.White.copy(alpha = 0.8f))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HeaderUserInfoPreview() {
    HeaderUserInfo()
}
