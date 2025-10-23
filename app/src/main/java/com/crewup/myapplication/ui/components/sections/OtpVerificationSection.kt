package com.crewup.myapplication.ui.screens.password

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

private val CrewUpBlue = Color(0xFF007BFF)

@Composable
fun OtpVerificationSection(
    email: String = "example@gmail.com",
    phoneNumber: String = "304 416 3214",
    onContinueClick: (String) -> Unit = {},
    navController: NavController
) {
    val otpLength = 6
    val otpValues = remember { mutableStateListOf("", "", "", "", "", "") }
    val focusRequesters = List(otpLength) { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Ingresa el código que enviamos a tu correo y número de teléfono:",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(email, style = MaterialTheme.typography.bodyMedium)
                Text(phoneNumber, style = MaterialTheme.typography.bodyMedium)

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (i in 0 until otpLength) {
                        OutlinedTextField(
                            value = otpValues[i],
                            onValueChange = { value ->
                                if (value.length <= 1 && value.all { it.isDigit() }) {
                                    otpValues[i] = value
                                    if (value.isNotEmpty() && i < otpLength - 1) {
                                        focusRequesters[i + 1].requestFocus()
                                    } else if (value.isEmpty() && i > 0) {
                                        focusRequesters[i - 1].requestFocus()
                                    }
                                }
                            },
                            modifier = Modifier
                                .width(42.dp)
                                .height(55.dp)
                                .focusRequester(focusRequesters[i]),
                            textStyle = TextStyle(fontSize = 22.sp, textAlign = TextAlign.Center),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CrewUpBlue,
                                unfocusedBorderColor = CrewUpBlue
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { onContinueClick(otpValues.joinToString("")) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = CrewUpBlue)
                ) {
                    Text("Continuar", color = Color.White)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewOtpVerificationScreen() {
    OtpVerificationSection(
        email = TODO(),
        phoneNumber = TODO(),
        onContinueClick = TODO(),
        navController = TODO()
    )
}


