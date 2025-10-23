package com.crewup.myapplication.data.repository

import android.net.Uri
import com.crewup.myapplication.models.Plan
import com.google.firebase.Timestamp
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

    /**
     * Crea un nuevo plan en Firestore con un ID generado automáticamente.
     *
     * @param plan El plan a crear
     * @return Result con el ID del plan creado o un error
     */
    suspend fun createPlan(plan: Plan): Result<String> {
        return try {
            val docRef = plansCollection.document()
            val newPlan = plan.copy(id = docRef.id, createdAt = Timestamp.now())
            docRef.set(newPlan).await()
            android.util.Log.d("PlanRepository", "Plan creado con ID: ${docRef.id}")
            Result.success(docRef.id)
        } catch (e: Exception) {
            android.util.Log.e("PlanRepository", "Error al crear plan: ${e.message}", e)
            Result.failure(Exception("Error al crear el plan: ${e.message}", e))
        }
    }

    /**
     * Actualiza campos específicos de un plan existente.
     *
     * @param planId ID del plan a actualizar
     * @param updates Mapa con los campos a actualizar
     * @return Result con Unit o un error
     */
    suspend fun updatePlan(planId: String, updates: Map<String, Any>): Result<Unit> {
        return try {
            plansCollection.document(planId).update(updates).await()
            android.util.Log.d("PlanRepository", "Plan actualizado: $planId")
            Result.success(Unit)
        } catch (e: Exception) {
            android.util.Log.e("PlanRepository", "Error al actualizar plan: ${e.message}", e)
            Result.failure(Exception("Error al actualizar el plan: ${e.message}", e))
        }
    }

    /**
     * Sube una imagen a Firebase Storage y actualiza la URL en Firestore.
     *
     * @param planId ID del plan al que pertenece la imagen
     * @param imageUri URI local de la imagen a subir
     * @return Result con la URL de descarga o un error
     */
    suspend fun uploadImageAndSaveUrl(planId: String, imageUri: Uri): Result<String> {
        require(planId.isNotBlank()) { "El planId no puede estar vacío" }
        require(imageUri != Uri.EMPTY) { "La URI de la imagen no puede estar vacía" }

        return try {
            android.util.Log.d("PlanRepository", "Storage bucket: ${storage.reference.bucket}")
            val storageRef = storage.reference.child("plans/$planId/${UUID.randomUUID()}.jpg")
            storageRef.putFile(imageUri).await()
            val downloadUrl = storageRef.downloadUrl.await().toString()
            plansCollection.document(planId).update(mapOf("imageUrl" to downloadUrl)).await()
            android.util.Log.d("PlanRepository", "Imagen subida: $downloadUrl")
            Result.success(downloadUrl)
        } catch (e: StorageException) {
            android.util.Log.e("PlanRepository", "Error al subir imagen: ${e.message}", e)
            Result.failure(Exception("Error al subir la imagen: ${e.message}", e))
        } catch (e: Exception) {
            android.util.Log.e("PlanRepository", "Error al procesar imagen: ${e.message}", e)
            Result.failure(Exception("Error al procesar la imagen: ${e.message}", e))
        }
    }
}