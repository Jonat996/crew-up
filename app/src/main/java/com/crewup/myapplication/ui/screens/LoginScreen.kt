package com.crewup.myapplication.ui.screens

import com.crewup.myapplication.ui.components.Form
import com.crewup.myapplication.ui.components.Header
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.crewup.myapplication.viewmodel.AuthState

@Composable
fun LoginScreen(
    authState: AuthState,
    onEmailLogin: (String, String) -> Unit,
    onGoogleLogin: () -> Unit,
    onClearError: () -> Unit
) {
    var isRegistering by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Header(title = "Iniciar Sesión")

        Box(
            modifier = Modifier
                .background(Color.White, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .padding(top = 16.dp)
        ) {
            Form(
                authState = authState,
                isRegistering = isRegistering,
                onEmailLogin = onEmailLogin,
                onGoogleLogin = onGoogleLogin,
                onClearError = onClearError
            )

            Spacer(Modifier.height(20.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom =24.dp),

            ) {
                Text(text = "¿No tienes una cuenta? ")
                Text(
                    text = "Crear una",
                    color = Color(0xFF0056B3),
                    fontWeight = FontWeight.SemiBold,
                    // modifier = Modifier.clickable(onClick = onCreateAccountClick)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewLoginScreen(){
    LoginScreen(
    authState= AuthState(),
    onEmailLogin = { email, pass -> },
    onGoogleLogin = {  },
    onClearError = {  }
)
}