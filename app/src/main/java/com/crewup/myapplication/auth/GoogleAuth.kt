package com.crewup.myapplication.auth

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.Credential
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.crewup.myapplication.R

class GoogleAuth(private val context: Context) {

    private val auth = FirebaseAuth.getInstance()

    /**
     * Genera la solicitud de credenciales para iniciar sesión con Google.
     */
    suspend fun getGoogleIdCredentialRequest(): GetCredentialRequest {
        val webClientId = context.getString(R.string.default_web_client_id)
        Log.d("GoogleAuth", "Using web client ID: $webClientId")

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(webClientId)
            .setAutoSelectEnabled(false)
            .build()

        return GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
    }

    /**
     * Maneja el resultado del inicio de sesión con Google usando Firebase Auth.
     */
    suspend fun signInWithGoogleCredential(
        response: GetCredentialResponse,
        onResult: (Boolean, String?) -> Unit
    ) {
        try {
            val credential = response.credential

            // Usar GoogleIdTokenCredential para extraer el token correctamente
            val googleIdTokenCredential = com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
                .createFrom(credential.data)

            val idToken = googleIdTokenCredential.idToken

            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(firebaseCredential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("GoogleAuth", "Login exitoso: ${task.result.user?.displayName}")
                        onResult(true, null)
                    } else {
                        Log.e("GoogleAuth", "Error en login de Firebase: ${task.exception?.message}")
                        onResult(false, task.exception?.message)
                    }
                }

        } catch (e: Exception) {
            Log.e("GoogleAuth", "Error al autenticar con Google", e)
            onResult(false, e.message)
        }
    }
}
