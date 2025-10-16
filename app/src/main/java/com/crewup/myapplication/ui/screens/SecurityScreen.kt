package com.crewup.myapplication.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.crewup.myapplication.ui.components.header.HeaderUserPhoto
import com.crewup.myapplication.ui.layout.MainLayout
import com.crewup.myapplication.viewmodel.AuthViewModel

@Composable
fun SecurityScreen(){
    MainLayout(
        header = { HeaderUserPhoto(title = "Seguridad")},
        content = {SecuritySection()}
    )
}

@Preview
@Composable
fun PreviewSecurityScreen(){
    SecurityScreen()
}
