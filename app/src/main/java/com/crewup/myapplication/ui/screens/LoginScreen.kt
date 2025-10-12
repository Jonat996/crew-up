package com.crewup.myapplication.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
    var isRegistering by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)
    ) {
        HeaderSection(isRegistering)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-50).dp)
        ) {
            LoginFormSection(
                authState = authState,
                isRegistering = isRegistering,
                onEmailLogin = onEmailLogin,
                onEmailRegister = onEmailRegister,
                onGoogleLogin = onGoogleLogin,
                onFacebookLogin = onFacebookLogin,
                onClearError = onClearError
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Texto fijo al fondo para alternar entre login y registro
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            Text(
                if (isRegistering)
                    "¿Ya tienes una cuenta?"
                else
                    "¿No tienes una cuenta?"
            )
            TextButton(onClick = { isRegistering = !isRegistering }) {
                Text(if (isRegistering) "Inicia sesión" else "Crear una")
            }
        }
    }
}

@Composable
fun HeaderSection(isRegistering: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(380.dp)
            .background(Color(0xFF1565C0)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.ic_pizza_logo),
                contentDescription = "Logo Pizza",
                modifier = Modifier
                    .size(300.dp)
                    .offset(y = (-30).dp)
            )

            Text(
                text = if (isRegistering) "Crear Cuenta" else "Iniciar Sesión",
                style = MaterialTheme.typography.headlineMedium.copy(color = Color.White),
                modifier = Modifier.offset(y = (-80).dp)
            )
        }
    }
}

@Composable
fun LoginFormSection(
    authState: AuthState,
    isRegistering: Boolean,
    onEmailLogin: (String, String) -> Unit,
    onEmailRegister: (String, String) -> Unit,
    onGoogleLogin: () -> Unit,
    onFacebookLogin: () -> Unit,
    onClearError: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var rememberData by remember { mutableStateOf(false) }

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
            // 🔹 Botón social solo si no está registrándose
            if (!isRegistering) {
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

                Spacer(modifier = Modifier.height(24.dp))
                Divider()
                Spacer(modifier = Modifier.height(24.dp))
            }

            // 🔹 Campos de correo y contraseña
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    if (authState.error != null) onClearError()
                },
                label = { Text("Correo electrónico") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    if (authState.error != null) onClearError()
                },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            // 🔹 Confirmar contraseña solo en modo registro
            if (isRegistering) {
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirmar contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (!isRegistering) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = rememberData,
                            onCheckedChange = { rememberData = it },
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Recordar datos", fontSize = 13.sp)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(onClick = { /* TODO: Recuperar contraseña */ }) {
                        Text("¿Olvidaste tu contraseña?", fontSize = 13.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

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
                onClick = {
                    if (isRegistering) {
                        if (password == confirmPassword) {
                            onEmailRegister(email, password)
                        }
                    } else {
                        onEmailLogin(email, password)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !authState.isLoading &&
                        email.isNotBlank() &&
                        password.isNotBlank() &&
                        (!isRegistering || confirmPassword.isNotBlank())
            ) {
                if (authState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp))
                } else {
                    Text(if (isRegistering) "Registrarse" else "Iniciar Sesión")
                }
            }
        }
    }
}
