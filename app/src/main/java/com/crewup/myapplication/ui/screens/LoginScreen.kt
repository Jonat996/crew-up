package com.crewup.myapplication.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.crewup.myapplication.R
import com.crewup.myapplication.viewmodel.AuthState

@Composable
fun LoginScreen(
    authState: AuthState,
    onEmailLogin: (String, String) -> Unit,
    onEmailRegister: (String, String) -> Unit,
    onGoogleLogin: () -> Unit,
    onFacebookLogin: () -> Unit,
    onClearError: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 🔹 Sección superior con logo
        HeaderSection()

        // 🔹 Formulario semitransparente sobre el fondo
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-50).dp)
            ) {
                LoginFormSection(
                    authState = authState,
                    onEmailLogin = onEmailLogin,
                    onEmailRegister = onEmailRegister,
                    onGoogleLogin = onGoogleLogin,
                    onFacebookLogin = onFacebookLogin,
                    onClearError = onClearError
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🔹 Texto "¿No tienes una cuenta?" fijo al fondo
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                Text("¿No tienes una cuenta?")
                TextButton(onClick = { onEmailRegister("", "") }) {
                    Text("Crear una")
                }
            }
        }
    }
}

@Composable
fun HeaderSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(380.dp) // 🔹 Bajamos más la parte azul
            .background(Color(0xFF1565C0)), // Azul principal
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // 🔹 Logo
            Image(
                painter = painterResource(id = R.drawable.ic_pizza_logo),
                contentDescription = "Logo Pizza",
                modifier = Modifier
                    .size(300.dp)
                    .offset(y = (-30).dp)
            )

            // 🔹 Texto principal más arriba
            Text(
                text = "Iniciar Sesión",
                style = MaterialTheme.typography.headlineMedium.copy(color = Color.White),
                modifier = Modifier.offset(y = (-80).dp)
            )
        }
    }
}

@Composable
fun LoginFormSection(
    authState: AuthState,
    onEmailLogin: (String, String) -> Unit,
    onEmailRegister: (String, String) -> Unit,
    onGoogleLogin: () -> Unit,
    onFacebookLogin: () -> Unit,
    onClearError: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberData by remember { mutableStateOf(false) }

    // 🔸 Contenedor del formulario (tarjeta blanca semitransparente)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .shadow(8.dp, shape = RoundedCornerShape(20.dp))
            .zIndex(1f),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🔹 Botón Google
            OutlinedButton(
                onClick = onGoogleLogin,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = "Google",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Continuar con Google")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 🔹 Botón Facebook
            OutlinedButton(
                onClick = onFacebookLogin,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_facebook),
                    contentDescription = "Facebook",
                    tint = Color(0xFF1877F2),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Continuar con Facebook")
            }

            Spacer(modifier = Modifier.height(24.dp))
            Divider()
            Spacer(modifier = Modifier.height(24.dp))

            // 🔹 Campo de correo
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    if (authState.error != null) onClearError()
                },
                label = { Text("Ingresa tu correo") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 🔹 Campo de contraseña
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    if (authState.error != null) onClearError()
                },
                label = { Text("Ingresa tu contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 🔹 Recordar datos / Olvidar contraseña ajustados
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = rememberData,
                        onCheckedChange = { rememberData = it },
                        modifier = Modifier.size(18.dp) // Checkbox más pequeño
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Recordar datos", fontSize = 13.sp)
                }
                Spacer(modifier = Modifier.weight(1f))
                TextButton(onClick = { /* TODO: Recuperar contraseña */ }) {
                    Text("¿Olvidaste tu contraseña?", fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🔹 Mostrar error
            if (authState.error != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                ) {
                    Text(
                        text = authState.error,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // 🔹 Botón principal
            Button(
                onClick = { onEmailLogin(email, password) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !authState.isLoading && email.isNotBlank() && password.isNotBlank()
            ) {
                if (authState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp))
                } else {
                    Text("Iniciar Sesión")
                }
            }
        }
    }
}
