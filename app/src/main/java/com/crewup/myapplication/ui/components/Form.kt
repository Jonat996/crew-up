package com.crewup.myapplication.ui.components


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crewup.myapplication.viewmodel.AuthState
import com.crewup.myapplication.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex


@Composable
fun Form(
    authState: AuthState,
    isRegistering: Boolean,
    onEmailLogin: (String, String) -> Unit,
    onEmailRegister: (String, String) -> Unit,
    onGoogleLogin: () -> Unit,
    onClearError: () -> Unit,
    modifier : Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var rememberData by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .shadow(8.dp, shape = RoundedCornerShape(10.dp))
            .zIndex(1f),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = modifier
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!isRegistering) {
                OutlinedButton(
                    onClick = onGoogleLogin,
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.Black,
                        containerColor = Color.White
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_google),
                        contentDescription = "Icono de Google",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Continúa con Google")
                }
                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Divider(Modifier.weight(1f), color = Color.LightGray)
                    Text("o", Modifier.padding(horizontal = 8.dp), color = Color.Gray)
                    Divider(Modifier.weight(1f), color = Color.LightGray)
                }

                // Campo de Correo
                OutlinedTextField(
                    value = email,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Gray,
                        unfocusedBorderColor = Color.Gray,
                        errorBorderColor = Color.Red
                    ),
                    onValueChange = {
                        email = it
                        if (authState.error != null) onClearError()
                    },
                    label = {
                        Text(
                            "Ingresa tu correo",
                            color = Color.Black,
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    shape = RoundedCornerShape(10.dp),
                )

                Spacer(Modifier.height(25.dp))

                // Campo de Contraseña
                OutlinedTextField(
                    value = password,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Gray,
                        unfocusedBorderColor = Color.Gray,
                        errorBorderColor = Color.Red
                    ),
                    onValueChange = {
                        password = it
                        if (authState.error != null) onClearError()
                    },
                    label = {
                        Text(
                            "Ingresa tu contraseña",
                            color = Color.Black,
                        )
                    },
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    shape = RoundedCornerShape(10.dp),
                    trailingIcon = {
                        val icon = if (showPassword) {
                            Icons.Filled.VisibilityOff
                        } else {
                            Icons.Filled.Visibility
                        }

                        Icon(
                            imageVector = icon,
                            contentDescription = "Mostrar contraseña",
                            modifier = Modifier.clickable { showPassword = !showPassword }
                        )
                    }
                )


                if (authState.error != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(y = (-50).dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),

                        ) {
                        Text(
                            text = authState.error,
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                    Spacer(modifier = Modifier.height(1.dp))
                }

                // 4. Recordar datos y Olvidaste tu contraseña
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = rememberData,
                            onCheckedChange = { rememberData = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color(0xFF0056B3),
                                uncheckedColor = Color.Gray,
                                checkmarkColor = Color.White
                            )
                        )
                        Text("Recordar datos", fontSize = 14.sp)
                    }
                    TextButton(onClick = { /* TODO: Recuperar contraseña */ }) {
                        Text(
                            text = "¿Olvidaste tu contraseña?",
                            color = Color(0xFF0056B3),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        )
                    }
                }

                // 5. Botón iniciar sesion
                Button(
                    onClick = {
                        if (isRegistering) {
                            // Si está registrando, llama a la función de registro
                            onEmailRegister(email, password)
                        } else {
                            onEmailLogin(email, password)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0056B3),
                        contentColor = Color.White
                    ),
                    enabled = !authState.isLoading &&
                            email.isNotBlank() &&
                            password.isNotBlank()
                ) {
                    if (authState.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp))
                    } else {
                        Text("Iniciar Sesión", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Preview (showSystemUi = true, showBackground = true)
@Composable
fun PreviewForm(){
    MaterialTheme {
        Form(
            authState = AuthState(),
            isRegistering = false,
            onEmailLogin = { email, pass -> },
            onEmailRegister = { email, pass -> },
            onGoogleLogin = {},
            onClearError = {}
        )
    }
}