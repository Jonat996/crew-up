package com.crewup.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.AuthCredential
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    private val _user = MutableStateFlow(auth.currentUser)
    val user: StateFlow<com.google.firebase.auth.FirebaseUser?> = _user

    fun signInWithCredential(credential: AuthCredential) {
        viewModelScope.launch {
            auth.signInWithCredential(credential)
                .addOnSuccessListener { result ->
                    _user.value = result.user
                }
                .addOnFailureListener { e ->
                    println("Error: ${e.message}")
                }
        }
    }

    fun signOut() {
        auth.signOut()
        _user.value = null
    }
}
