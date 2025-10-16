package com.crewup.myapplication.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.crewup.myapplication.ui.components.EditProfileSection
import com.crewup.myapplication.ui.components.FormRegister
import com.crewup.myapplication.ui.components.header.HeaderUserInfo
import com.crewup.myapplication.ui.layout.MainLayout

@Composable
fun EditProfileScreen(){
    MainLayout(
        header = { HeaderUserInfo() },
        content = { EditProfileSection() }
    )
}

@Preview
@Composable
fun PreviewEditProfileScreen(){
    EditProfileScreen()
}