package com.crewup.myapplication.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.crewup.myapplication.ui.components.header.HeaderUserInfo
import com.crewup.myapplication.ui.components.sections.EditProfileSection
import com.crewup.myapplication.ui.layout.MainLayout

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(navController: NavController) {
    MainLayout(
        header = {
            HeaderUserInfo(navController = navController)
        },
        content = {
            EditProfileSection()
        }
    )
}
