package com.crewup.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(onEmailLogin: (String, String) -> Unit, onGoogleLogin: () -> Unit, onFacebookLogin: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        TextField(value = password, onValueChange = { password = it }, label = { Text("Password") })

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onEmailLogin(email, password) }) { Text("Login") }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onGoogleLogin() }) { Text("Login with Google") }

        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { onFacebookLogin() }) { Text("Login with Facebook") }
    }
}
