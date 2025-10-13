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
fun FormRegister(
    authState: AuthState,
    isRegistering: Boolean,
    onEmailLogin: (String, String) -> Unit,
    onGoogleLogin: () -> Unit,
    onClearError: () -> Unit,
    modifier : Modifier = Modifier
) {
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .shadow(8.dp, shape = RoundedCornerShape(10.dp))
            .zIndex(1f),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(10.dp)
    ){
        Column(modifier = modifier
            .padding(horizontal = 24.dp)) {
                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                }
                //Campo nombre y apellido
                Row (modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically){
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = {
                            nombre = it
                            if (authState.error != null) onClearError()
                        },
                        label = {
                            Text("Ingresa tu Nombre",
                                color = Color.Black,
                            ) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        shape = RoundedCornerShape(10.dp),
                    )
                    OutlinedTextField(
                        value = apellido,
                        onValueChange = {
                            apellido = it
                            if (authState.error != null) onClearError()
                        },
                        label = {
                            Text("Ingresa tu a",
                                color = Color.Black,
                            ) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        shape = RoundedCornerShape(10.dp),
                    )
                }

                // Campo de Correo
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        if (authState.error != null) onClearError()
                    },
                    label = {
                        Text("Ingresa tu correo",
                            color = Color.Black,
                        ) },
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
                    onValueChange = {
                        password = it
                        if (authState.error != null) onClearError()
                    },
                    label = { Text("Ingresa tu contraseña") },
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
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
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    shape = RoundedCornerShape(10.dp)
                )
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


                    Text(
                        text = "¿Olvidaste tu contraseña?",
                        color = Color(0xFF0056B3),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                        //modifier = Modifier.clickable(onClick = onForgotPasswordClick)
                    )
                }

                // 5. Botón principal iniciar sesion
                Button(
                    onClick = { onEmailLogin(email, password) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0056B3),
                        contentColor = Color.White
                    )
                ) {
                    Text("Registrar", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Preview (showSystemUi = true, showBackground = true)
@Composable
fun PreviewFormRegister(){
    MaterialTheme {
        FormRegister(
            authState = AuthState(),
            isRegistering = false,
            onEmailLogin = { email, pass -> },
            onGoogleLogin = {},
            onClearError = {}
        )
    }
}