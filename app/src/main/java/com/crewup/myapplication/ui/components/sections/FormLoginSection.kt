package com.crewup.myapplication.ui.components.sections

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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import androidx.compose.ui.res.stringResource

@Composable
fun Form(
    authState: AuthState,
    isRegistering: Boolean,
    onEmailLogin: (String, String) -> Unit,
    onEmailRegister: (String, String, String, String, String, String, String) -> Unit,
    onGoogleLogin: () -> Unit,
    onClearError: () -> Unit,
    modifier: Modifier = Modifier,
    onForgotPassword: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var rememberData by remember { mutableStateOf(false) }

    //  Campos adicionales del registro
    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

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

            // 🔹 Botón de Google y separador (solo en login)
            if (!isRegistering) {
                OutlinedButton(
                    onClick = onGoogleLogin,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.Black,
                        containerColor = Color.White
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_google),
                        contentDescription = stringResource(R.string.google_icon_description),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.continue_with_google))
                }

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Divider(Modifier.weight(1f), color = Color.LightGray)
                    Text(
                        stringResource(R.string.or_separator),
                        Modifier.padding(horizontal = 8.dp),
                        color = Color.Gray
                    )
                    Divider(Modifier.weight(1f), color = Color.LightGray)
                }
            }

            // 🔹 Campos visibles siempre (correo y contraseña)
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    if (authState.error != null) onClearError()
                },
                label = { Text(stringResource(R.string.enter_email), color = Color.Black) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray,
                    errorBorderColor = Color.Red
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(Modifier.height(25.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    if (authState.error != null) onClearError()
                },
                label = { Text(stringResource(R.string.enter_password), color = Color.Black) },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray,
                    errorBorderColor = Color.Red
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                shape = RoundedCornerShape(10.dp),
                trailingIcon = {
                    val icon = if (showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                    Icon(
                        imageVector = icon,
                        contentDescription = stringResource(R.string.show_password),
                        modifier = Modifier.clickable { showPassword = !showPassword }
                    )
                }
            )

            // 🔹 Campos adicionales (solo si es registro)
            if (isRegistering) {
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Apellido") },
                    modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(value = country, onValueChange = { country = it }, label = { Text("País") },
                    modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(value = city, onValueChange = { city = it }, label = { Text("Ciudad") },
                    modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Teléfono") },
                    modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(10.dp))
            }

            // 🔹 Mostrar error si existe
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

            // 🔹 Recordar datos y olvidar contraseña (solo login)
            if (!isRegistering) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
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
                        Text(stringResource(R.string.remember_data), fontSize = 14.sp)
                    }
                    TextButton(onClick = onForgotPassword) {
                        Text(
                            text = stringResource(R.string.forgot_password),
                            color = Color(0xFF0056B3),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // 🔹 Botón principal
            Button(
                onClick = {
                    if (isRegistering) {
                        onEmailRegister(
                            name,
                            lastName,
                            email,
                            password,
                            country,
                            city,
                            phone
                        )
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
                    Text(
                        if (isRegistering) "Registrarse" else stringResource(R.string.login_button),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewForm() {
    MaterialTheme {
        Form(
            authState = AuthState(),
            isRegistering = false,
            onEmailLogin = { _, _ -> },
            onEmailRegister = { _, _, _, _, _, _, _ -> },
            onGoogleLogin = {},
            onClearError = {}
        )
    }
}
