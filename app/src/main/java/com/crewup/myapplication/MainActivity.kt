package com.crewup.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.credentials.CredentialManager
import androidx.lifecycle.lifecycleScope
import com.crewup.myapplication.auth.FacebookAuth
import com.crewup.myapplication.auth.GoogleAuth
import com.crewup.myapplication.ui.screens.HomeScreen
import com.crewup.myapplication.ui.screens.LoginScreen
import com.crewup.myapplication.ui.theme.CrewUpTheme
import com.crewup.myapplication.viewmodel.AuthViewModel
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var googleAuth: GoogleAuth
    private lateinit var facebookAuth: FacebookAuth
    private val authViewModel: AuthViewModel by viewModels()

    private val facebookLoginLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        facebookAuth.callbackManager.onActivityResult(
            result.resultCode,
            result.resultCode,
            result.data
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        googleAuth = GoogleAuth(this)
        facebookAuth = FacebookAuth(this)

        setContent {
            CrewUpTheme {
                val authState by authViewModel.authState.collectAsState()

                if (authState.isAuthenticated) {
                    HomeScreen(
                        user = authState.user,
                        onSignOut = { authViewModel.signOut() }
                    )
                } else {
                    LoginScreen(
                        authState = authState,
                        onEmailLogin = { email, password ->
                            authViewModel.signInWithEmailPassword(email, password)
                        },
                        onEmailRegister = { email, password ->
                            authViewModel.registerWithEmailPassword(email, password)
                        },
                        onGoogleLogin = {
                            lifecycleScope.launch {
                                try {
                                    val credentialManager = CredentialManager.create(this@MainActivity)
                                    val request = googleAuth.getGoogleIdCredentialRequest()

                                    val response = credentialManager.getCredential(
                                        context = this@MainActivity,
                                        request = request
                                    )

                                    googleAuth.signInWithGoogleCredential(response) { success, message ->
                                        if (success) {
                                            val credential = GoogleAuthProvider.getCredential(
                                                com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
                                                    .createFrom(response.credential.data).idToken,
                                                null
                                            )
                                            authViewModel.signInWithCredential(credential)
                                        } else {
                                            println("❌ Google credential error: $message")
                                        }
                                    }
                                } catch (e: androidx.credentials.exceptions.GetCredentialException) {
                                    println("❌ Google Sign-In GetCredentialException: ${e.message}")
                                    println("❌ Error type: ${e.type}")
                                } catch (e: Exception) {
                                    println("❌ Google Sign-In failed: ${e.message}")
                                    e.printStackTrace()
                                }
                            }
                        },
                        onFacebookLogin = {
                            facebookAuth.login { success, message ->
                                if (success) {
                                    println("✅ Facebook login exitoso")
                                } else {
                                    println("❌ Facebook login error: $message")
                                }
                            }
                        },
                        onClearError = {
                            authViewModel.clearError()
                        }
                    )
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        facebookAuth.callbackManager.onActivityResult(requestCode, resultCode, data)
    }
}
