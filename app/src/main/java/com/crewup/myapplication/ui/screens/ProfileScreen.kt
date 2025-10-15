package com.crewup.myapplication.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.crewup.myapplication.ui.components.Header
import com.crewup.myapplication.viewmodel.AuthState

@Composable
fun ProfileScreen (
    authState: AuthState
) {
    Header(title = authState.user?.displayName ?: "CrewUlp")
}


@Preview (showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(authState = AuthState())
}