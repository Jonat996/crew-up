package com.crewup.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.lifecycle.lifecycleScope
import com.crewup.myapplication.auth.GoogleAuth
import com.crewup.myapplication.ui.navigation.AppNavigation
import com.crewup.myapplication.ui.theme.CrewUpTheme
import com.crewup.myapplication.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var googleAuth: GoogleAuth
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        googleAuth = GoogleAuth(this)

        setContent {
            CrewUpTheme {
                val authState by authViewModel.authState.collectAsState()
                AppNavigation(
                    authState = authState,
                    authViewModel = authViewModel,
                    googleAuth = googleAuth
                )


            }
        }

    }
}