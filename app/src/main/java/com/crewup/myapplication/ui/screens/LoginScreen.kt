package com.crewup.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
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
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isRegistering by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isRegistering) "Crear Cuenta" else "Iniciar Sesión",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                if (authState.error != null) onClearError()
            },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            enabled = !authState.isLoading
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
            modifier = Modifier.fillMaxWidth(),
            enabled = !authState.isLoading
        )

        Spacer(modifier = Modifier.height(24.dp))

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

        Button(
            onClick = {
                if (isRegistering) {
                    onEmailRegister(email, password)
                } else {
                    onEmailLogin(email, password)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !authState.isLoading && email.isNotBlank() && password.isNotBlank()
        ) {
            if (authState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(16.dp))
            } else {
                Text(if (isRegistering) "Registrarse" else "Iniciar Sesión")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = { isRegistering = !isRegistering },
            enabled = !authState.isLoading
        ) {
            Text(
                if (isRegistering) "¿Ya tienes cuenta? Inicia sesión"
                else "¿No tienes cuenta? Regístrate"
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        HorizontalDivider()

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onGoogleLogin,
            modifier = Modifier.fillMaxWidth(),
            enabled = !authState.isLoading
        ) {
            Text("Continuar con Google")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = onFacebookLogin,
            modifier = Modifier.fillMaxWidth(),
            enabled = !authState.isLoading
        ) {
            Text("Continuar con Facebook")
        }
    }
}
