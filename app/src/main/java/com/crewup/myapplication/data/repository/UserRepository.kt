package com.crewup.myapplication.data.repository

import com.crewup.myapplication.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    private val usersCollection = db.collection("users")

    // Obtener usuario actual
    suspend fun getCurrentUser(): Result<User?> {
        println("=== getCurrentUser() llamado ===")
        val uid = auth.currentUser?.uid
        println("UID del usuario autenticado: $uid")

        if (uid == null) {
            println("ERROR: No hay usuario autenticado")
            return Result.failure(Exception("No autenticado"))
        }

        return try {
            println("Intentando obtener documento de Firestore para uid: $uid")
            val doc = usersCollection.document(uid).get().await()
            println("Documento obtenido. Existe: ${doc.exists()}")
            println("Datos del documento: ${doc.data}")

            val user = doc.toObject(User::class.java)
            println("Usuario convertido: $user")

            Result.success(user)
        } catch (e: Exception) {
            println("ERROR al obtener usuario de Firestore: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }

    // Actualizar perfil (merge para no sobrescribir todo)
    suspend fun updateUserProfile(updates: Map<String, Any>): Result<Unit> {
        val uid = auth.currentUser?.uid ?: return Result.failure(Exception("No autenticado"))
        return try {
            usersCollection.document(uid).update(updates).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Crear usuario inicial (llamado post-registro)
    suspend fun createUser(user: User): Result<Unit> {
        return try {
            usersCollection.document(user.uid).set(user).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Actualizar Firebase Auth (email y displayName)
    suspend fun updateFirebaseAuth(
        displayName: String? = null,
        email: String? = null
    ): Result<Unit> {
        val currentUser = auth.currentUser ?: return Result.failure(Exception("No autenticado"))

        return try {
            // Actualizar displayName si se proporciona
            if (displayName != null) {
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .build()
                currentUser.updateProfile(profileUpdates).await()
            }

            // Actualizar email si se proporciona
            if (email != null && email != currentUser.email) {
                currentUser.updateEmail(email).await()
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
