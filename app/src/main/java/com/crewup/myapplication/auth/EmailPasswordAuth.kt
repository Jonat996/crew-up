package com.crewup.myapplication.auth

import com.google.firebase.auth.FirebaseAuth

class EmailPasswordAuth(private val auth: FirebaseAuth = FirebaseAuth.getInstance()) {

    fun register(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                onResult(task.isSuccessful, task.exception?.message)
            }
    }

    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                onResult(task.isSuccessful, task.exception?.message)
            }
    }

    fun sendPasswordResetEmail(email: String, onResult: (Boolean, String?) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, "Correo de recuperación enviado exitosamente")
                } else {
                    onResult(false, task.exception?.message ?: "Error al enviar el correo")
                }
            }
    }
}
