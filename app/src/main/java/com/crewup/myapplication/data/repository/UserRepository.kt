package com.crewup.myapplication.data.repository

import android.net.Uri
import com.crewup.myapplication.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import kotlinx.coroutines.tasks.await
import java.util.UUID

class UserRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
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

    /**
     * Sube una foto de perfil a Firebase Storage y actualiza la URL en Firestore.
     *
     * @param imageUri URI local de la imagen a subir
     * @return Result con la URL de descarga o un error
     */
    suspend fun uploadUserPhotoAndSaveUrl(imageUri: Uri): Result<String> {
        val uid = auth.currentUser?.uid ?: return Result.failure(Exception("No autenticado"))
        require(imageUri != Uri.EMPTY) { "La URI de la imagen no puede estar vacía" }

        return try {
            android.util.Log.d("UserRepository", "Storage bucket: ${storage.reference.bucket}")
            val storageRef = storage.reference.child("users/$uid/profile/${UUID.randomUUID()}.jpg")
            storageRef.putFile(imageUri).await()
            val downloadUrl = storageRef.downloadUrl.await().toString()
            usersCollection.document(uid).update(mapOf("photoUrl" to downloadUrl)).await()
            android.util.Log.d("UserRepository", "Foto de perfil subida: $downloadUrl")
            Result.success(downloadUrl)
        } catch (e: StorageException) {
            android.util.Log.e("UserRepository", "Error al subir foto de perfil: ${e.message}", e)
            Result.failure(Exception("Error al subir la foto de perfil: ${e.message}", e))
        } catch (e: Exception) {
            android.util.Log.e("UserRepository", "Error al procesar foto de perfil: ${e.message}", e)
            Result.failure(Exception("Error al procesar la foto de perfil: ${e.message}", e))
        }
    }
}
