package com.crewup.myapplication.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.AuthCredential
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.crewup.myapplication.auth.GoogleAuth
import com.crewup.myapplication.auth.EmailPasswordAuth
data class AuthState(
    val isLoading: Boolean = false,
    val user: com.google.firebase.auth.FirebaseUser? = null,
    val error: String? = null,
    val isAuthenticated: Boolean = false
)

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val emailPasswordAuth = EmailPasswordAuth()

    private val _authState = MutableStateFlow(
        AuthState(
            user = auth.currentUser,
            isAuthenticated = auth.currentUser != null
        )
    )
    val authState: StateFlow<AuthState> = _authState

    // Listener para detectar cambios en el estado de autenticación de Firebase
    private val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        val currentUser = firebaseAuth.currentUser
        _authState.value = _authState.value.copy(
            user = currentUser,
            isAuthenticated = currentUser != null,
            isLoading = false
        )
    }

    init {
        // Registrar el listener cuando se crea el ViewModel
        auth.addAuthStateListener(authStateListener)
    }

    override fun onCleared() {
        super.onCleared()
        // Remover el listener cuando se destruye el ViewModel
        auth.removeAuthStateListener(authStateListener)
    }

    fun signInWithCredential(credential: AuthCredential) {
        _authState.value = _authState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            auth.signInWithCredential(credential)
                .addOnSuccessListener { result ->
                    _authState.value = _authState.value.copy(
                        isLoading = false,
                        user = result.user,
                        isAuthenticated = true,
                        error = null
                    )
                }
                .addOnFailureListener { e ->
                    _authState.value = _authState.value.copy(
                        isLoading = false,
                        error = e.message,
                        isAuthenticated = false
                    )
                }
        }
    }

    fun signInWithEmailPassword(email: String, password: String) {
        _authState.value = _authState.value.copy(isLoading = true, error = null)

        emailPasswordAuth.login(email, password) { success, message ->
            if (success) {
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    user = auth.currentUser,
                    isAuthenticated = true,
                    error = null
                )
            } else {
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    error = message,
                    isAuthenticated = false
                )
            }
        }
    }

    fun registerWithEmailPassword(email: String, password: String) {
        _authState.value = _authState.value.copy(isLoading = true, error = null)

        emailPasswordAuth.register(email, password) { success, message ->
            if (success) {
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    user = auth.currentUser,
                    isAuthenticated = true,
                    error = null
                )
            } else {
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    error = message,
                    isAuthenticated = false
                )
            }
        }
    }

    fun signOut() {
        auth.signOut()
        _authState.value = AuthState()
    }

    fun clearError() {
        _authState.value = _authState.value.copy(error = null)
    }

}
