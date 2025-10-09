package com.crewup.myapplication.auth

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.crewup.myapplication.R
import kotlinx.coroutines.tasks.await

class GoogleAuth(private val context: Context) {

    private val auth = FirebaseAuth.getInstance()
    private val credentialManager = CredentialManager.create(context)

    /**
     * Inicia sesión con Google usando Credential Manager y Firebase Auth.
     * Según la documentación oficial de Firebase y Android.
     */
    suspend fun signInWithGoogle(): Result<com.google.firebase.auth.FirebaseUser?> {
        return try {
            // 1. Obtener el Web Client ID desde strings.xml
            val webClientId = context.getString(R.string.default_web_client_id)
            Log.d("GoogleAuth", "Web Client ID: $webClientId")
            Log.d("GoogleAuth", "Package Name: ${context.packageName}")

            // 2. Configurar la opción de Google ID según la documentación oficial
            // Primero intentamos sin filtrar cuentas autorizadas
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false) // Mostrar todas las cuentas
                .setServerClientId(webClientId)
                .setAutoSelectEnabled(false) // Desactivar auto-select para debug
                .build()

            // 3. Crear la solicitud de credenciales
            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            Log.d("GoogleAuth", "Solicitando credenciales al Credential Manager...")

            // 4. Obtener las credenciales del usuario
            val result = credentialManager.getCredential(
                context = context,
                request = request
            )

            Log.d("GoogleAuth", "Credenciales recibidas, procesando...")

            // 5. Manejar la respuesta
            handleSignIn(result)

        } catch (e: androidx.credentials.exceptions.NoCredentialException) {
            Log.e("GoogleAuth", "No hay credenciales disponibles. Posibles causas:")
            Log.e("GoogleAuth", "1. SHA-1 no registrado en Firebase Console")
            Log.e("GoogleAuth", "2. Google Sign-In no habilitado en Firebase")
            Log.e("GoogleAuth", "3. Web Client ID incorrecto")
            Log.e("GoogleAuth", "4. Google Play Services desactualizado")
            Result.failure(e)
        } catch (e: Exception) {
            Log.e("GoogleAuth", "Error durante Google Sign-In: ${e.javaClass.simpleName}", e)
            Result.failure(e)
        }
    }

    /**
     * Maneja el resultado del sign-in y autentica con Firebase.
     */
    private suspend fun handleSignIn(result: GetCredentialResponse): Result<com.google.firebase.auth.FirebaseUser?> {
        return try {
            val credential = result.credential

            // Verificar que sea una credencial de Google
            if (credential is CustomCredential &&
                credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {

                // Extraer el token de Google
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val idToken = googleIdTokenCredential.idToken

                Log.d("GoogleAuth", "Google ID Token obtenido exitosamente")
                Log.d("GoogleAuth", "Usuario: ${googleIdTokenCredential.displayName}")

                // Autenticar con Firebase usando el token de Google
                val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                val authResult = auth.signInWithCredential(firebaseCredential).await()

                Log.d("GoogleAuth", "Firebase Auth exitoso: ${authResult.user?.displayName}")
                Result.success(authResult.user)
            } else {
                Log.e("GoogleAuth", "Tipo de credencial inesperado: ${credential.type}")
                Result.failure(Exception("Tipo de credencial inválido"))
            }
        } catch (e: Exception) {
            Log.e("GoogleAuth", "Error al procesar credenciales", e)
            Result.failure(e)
        }
    }
}
