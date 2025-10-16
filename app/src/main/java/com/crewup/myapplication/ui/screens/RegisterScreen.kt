package com.crewup.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.crewup.myapplication.ui.components.FormRegister
import com.crewup.myapplication.ui.components.Header
import com.crewup.myapplication.viewmodel.AuthState

@Composable
fun RegisterScreen(
    authState: AuthState,
    onEmailRegister: (String, String) -> Unit,
    onGoogleLogin: () -> Unit,
    onClearError: () -> Unit,
    onNavigateToLogin: () -> Unit // 👈 para volver al login si se quiere
) {
    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 60.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🔹 Usa el mismo header pero cambia el título
            Header(title = "Crear cuenta")

            Box(
                modifier = Modifier
                    .offset(y = (-40).dp)
                    .padding(top = 10.dp)
            ) {
                FormRegister(
                    authState = authState,
                    isRegistering = true, // ✅ Siempre en modo registro
                    onEmailRegister = onEmailRegister,
                    onGoogleLogin = onGoogleLogin,
                    onClearError = onClearError
                )
            }
        }

        // 🔹 Texto para volver al login
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(vertical = 32.dp, horizontal = 0.dp)
        ) {
            Text(text = "¿Ya tienes una cuenta? ")

            TextButton(onClick = { onNavigateToLogin() }) {
                Text(
                    text = "Inicia sesión",
                    color = Color(0xFF0056B3),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewRegisterScreen() {
    RegisterScreen(
        authState = AuthState(),
        onEmailRegister = { _, _ -> },
        onGoogleLogin = {},
        onClearError = {},
        onNavigateToLogin = {}
    )
}
