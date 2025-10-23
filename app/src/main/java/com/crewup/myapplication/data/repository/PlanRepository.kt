package com.crewup.myapplication.data.repository

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import kotlinx.coroutines.tasks.await
import java.util.UUID

class PlanRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
) {
    private val plansCollection = db.collection("plans")

    suspend fun uploadImageAndSaveUrl(planId: String, imageUri: Uri): Result<String> {
        require(planId.isNotBlank()) { "El planId no puede estar vacío" }
        require(imageUri != Uri.EMPTY) { "La URI de la imagen no puede estar vacía" }

        return try {
            android.util.Log.d("PlanRepository", "Storage bucket: ${storage.reference.bucket}")
            val storageRef = storage.reference.child("plans/$planId/${UUID.randomUUID()}.jpg")
            storageRef.putFile(imageUri).await()
            val downloadUrl = storageRef.downloadUrl.await().toString()
            plansCollection.document(planId).update(mapOf("imageUrl" to downloadUrl)).await()
            Result.success(downloadUrl)
        } catch (e: StorageException) {
            Result.failure(Exception("Error al subir la imagen: ${e.message}", e))
        } catch (e: Exception) {
            Result.failure(Exception("Error al procesar la imagen: ${e.message}", e))
        }
    }
}