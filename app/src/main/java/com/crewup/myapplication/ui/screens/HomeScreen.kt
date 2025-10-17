package com.crewup.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.crewup.myapplication.ui.components.BottomNavBar
import com.crewup.myapplication.ui.navigation.Routes
import com.google.firebase.auth.FirebaseUser
import androidx.compose.ui.res.stringResource
import com.crewup.myapplication.R

@Composable
fun HomeScreen(
    user: FirebaseUser?,
    navController: NavController
) {
    Scaffold(
        bottomBar = { BottomNavBar(navController = navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
        Text(
            text = stringResource(R.string.welcome_crewup),
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        user?.let {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.user_label),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = it.displayName ?: stringResource(R.string.no_name),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.email_label),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = it.email ?: stringResource(R.string.no_email),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { navController.navigate(Routes.Profile.route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.go_to_profile))
        }
        }
    }
}