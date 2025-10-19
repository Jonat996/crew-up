package com.crewup.myapplication.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.crewup.myapplication.R
import com.crewup.myapplication.ui.components.header.HeaderBase
import com.crewup.myapplication.ui.components.header.HeaderUserPhoto
import com.crewup.myapplication.ui.layout.MainLayout
import com.crewup.myapplication.viewmodel.AuthViewModel

@Composable
fun NotificationsScreen(
    navController: NavController,
){
    val notifications = stringResource(R.string.notifications)

    MainLayout(
        header = {
            HeaderBase(navController = navController) {
                HeaderUserPhoto(title = notifications)
            }
        },
        content = { NotificationsSection() }
    )
}

@Preview
@Composable
fun PreviewNotificationsScreen(){
    NotificationsScreen(navController = rememberNavController())
}
