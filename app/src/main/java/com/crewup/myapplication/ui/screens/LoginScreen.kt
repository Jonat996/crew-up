package com.crewup.myapplication.ui.screens

import com.crewup.myapplication.ui.components.sections.Form
import com.crewup.myapplication.ui.components.Header
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.crewup.myapplication.auth.GoogleAuth
import com.crewup.myapplication.viewmodel.AuthState
import androidx.compose.ui.res.stringResource
import com.crewup.myapplication.R

@Composable
fun LoginScreen(
    authState: AuthState,
    onEmailLogin: (String, String) -> Unit,
    onEmailRegister: (String, String) -> Unit,
    onGoogleLogin: () -> Unit,
    onClearError: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var isRegistering by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val googleAuth = remember { GoogleAuth(context) }
    val scrollState = rememberScrollState()

Box(modifier = Modifier.fillMaxSize()) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 60.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header(title = stringResource(R.string.login_title))

        Box(
            modifier = Modifier
                .offset(y = (-40).dp)
                .padding(top = 10.dp)
        ) {
            Form(
                authState = authState,
                isRegistering = isRegistering,
                onEmailLogin = onEmailLogin,
                onEmailRegister = onEmailRegister,
                onGoogleLogin = onGoogleLogin,
                onClearError = onClearError
            )
        }
    }
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .padding(vertical = 32.dp, horizontal = 0.dp)

    ) {
            Text(text = stringResource(R.string.no_account) + " ")

            TextButton(onClick = onNavigateToRegister){
                Text(
                    text = stringResource(R.string.create_one),
                    color = Color(0xFF0056B3),
                    fontWeight = FontWeight.SemiBold,
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
        onEmailRegister = {email, pass ->},
        onGoogleLogin = {  },
        onClearError = {  },
        onNavigateToRegister = {  }
    )
}