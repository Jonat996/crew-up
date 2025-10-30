package com.crewup.myapplication.data.repository

import android.net.Uri
import com.crewup.myapplication.models.Plan
import com.crewup.myapplication.models.PlanUser
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
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

    /**
     * Obtiene un plan específico por su ID.
     *
     * @param planId ID del plan a obtener
     * @return Result con el Plan o un error si no existe
     */
    suspend fun getPlanById(planId: String): Result<Plan> {
        return try {
            val snapshot = plansCollection.document(planId).get().await()
            if (snapshot.exists()) {
                val plan = snapshot.toObject(Plan::class.java)
                if (plan != null) {
                    android.util.Log.d("PlanRepository", "Plan obtenido: $planId")
                    Result.success(plan)
                } else {
                    Result.failure(Exception("Error al deserializar el plan"))
                }
            } else {
                Result.failure(Exception("El plan no existe"))
            }
        } catch (e: Exception) {
            android.util.Log.e("PlanRepository", "Error al obtener plan: ${e.message}", e)
            Result.failure(Exception("Error al obtener el plan: ${e.message}", e))
        }
    }

    /**
     * Obtiene múltiples planes con filtros opcionales.
     *
     * @param limit Número máximo de planes a obtener (por defecto 50)
     * @param tags Lista de tags para filtrar (vacía = sin filtro)
     * @param city Ciudad para filtrar (null = sin filtro)
     * @param orderByDate Si true, ordena por fecha ascendente
     * @return Result con lista de planes o un error
     */
    suspend fun getPlans(
        limit: Int = 50,
        tags: List<String> = emptyList(),
        city: String? = null,
        orderByDate: Boolean = true
    ): Result<List<Plan>> {
        return try {
            var query: Query = plansCollection

            // Filtrar por ciudad si se proporciona
            if (!city.isNullOrBlank()) {
                query = query.whereEqualTo("location.city", city)
            }

            // Ordenar por fecha
            if (orderByDate) {
                query = query.orderBy("date", Query.Direction.ASCENDING)
            } else {
                query = query.orderBy("createdAt", Query.Direction.DESCENDING)
            }

            // Aplicar límite
            query = query.limit(limit.toLong())

            val snapshot = query.get().await()
            var plans = snapshot.toObjects(Plan::class.java)

            // Filtrar por tags en memoria si se proporcionan (Firestore no soporta array-contains-any con múltiples condiciones)
            if (tags.isNotEmpty()) {
                plans = plans.filter { plan ->
                    plan.tags.any { tag -> tags.contains(tag) }
                }
            }

            android.util.Log.d("PlanRepository", "Planes obtenidos: ${plans.size}")
            Result.success(plans)
        } catch (e: Exception) {
            android.util.Log.e("PlanRepository", "Error al obtener planes: ${e.message}", e)
            Result.failure(Exception("Error al obtener planes: ${e.message}", e))
        }
    }

    /**
     * Obtiene los planes creados por un usuario específico.
     *
     * @param userId UID del usuario creador
     * @return Result con lista de planes o un error
     */
    suspend fun getUserPlans(userId: String): Result<List<Plan>> {
        return try {
            val query = plansCollection
                .whereEqualTo("createdBy.uid", userId)
                .orderBy("createdAt", Query.Direction.DESCENDING)

            val snapshot = query.get().await()
            val plans = snapshot.toObjects(Plan::class.java)

            android.util.Log.d("PlanRepository", "Planes del usuario obtenidos: ${plans.size}")
            Result.success(plans)
        } catch (e: Exception) {
            android.util.Log.e("PlanRepository", "Error al obtener planes del usuario: ${e.message}", e)
            Result.failure(Exception("Error al obtener planes del usuario: ${e.message}", e))
        }
    }

    /**
     * Obtiene los planes en los que un usuario está participando.
     *
     * @param userId UID del usuario participante
     * @return Result con lista de planes o un error
     */
    suspend fun getUserParticipatingPlans(userId: String): Result<List<Plan>> {
        return try {
            val query = plansCollection
                .whereArrayContains("participants", mapOf("uid" to userId))
                .orderBy("date", Query.Direction.ASCENDING)

            val snapshot = query.get().await()
            val plans = snapshot.toObjects(Plan::class.java)

            android.util.Log.d("PlanRepository", "Planes donde participa el usuario: ${plans.size}")
            Result.success(plans)
        } catch (e: Exception) {
            android.util.Log.e("PlanRepository", "Error al obtener planes participantes: ${e.message}", e)
            Result.failure(Exception("Error al obtener planes donde participa: ${e.message}", e))
        }
    }

    /**
     * Elimina un plan de Firestore.
     * También elimina la imagen asociada de Storage si existe.
     *
     * @param planId ID del plan a eliminar
     * @return Result con Unit o un error
     */
    suspend fun deletePlan(planId: String): Result<Unit> {
        return try {
            // Primero obtenemos el plan para obtener la URL de la imagen
            val planResult = getPlanById(planId)
            if (planResult.isSuccess) {
                val plan = planResult.getOrNull()

                // Intentar eliminar la imagen si existe
                if (plan != null && plan.imageUrl.isNotBlank()) {
                    try {
                        val imageRef = storage.getReferenceFromUrl(plan.imageUrl)
                        imageRef.delete().await()
                        android.util.Log.d("PlanRepository", "Imagen del plan eliminada")
                    } catch (e: Exception) {
                        android.util.Log.w("PlanRepository", "No se pudo eliminar la imagen: ${e.message}")
                        // Continuar con la eliminación del plan aunque falle la imagen
                    }
                }
            }

            // Eliminar el documento del plan
            plansCollection.document(planId).delete().await()
            android.util.Log.d("PlanRepository", "Plan eliminado: $planId")
            Result.success(Unit)
        } catch (e: Exception) {
            android.util.Log.e("PlanRepository", "Error al eliminar plan: ${e.message}", e)
            Result.failure(Exception("Error al eliminar el plan: ${e.message}", e))
        }
    }

    /**
     * Agrega un usuario a la lista de participantes de un plan.
     *
     * @param planId ID del plan
     * @param user Usuario a agregar
     * @return Result con Unit o un error
     */
    suspend fun joinPlan(planId: String, user: PlanUser): Result<Unit> {
        return try {
            // Verificar que el usuario no esté ya en el plan
            val planResult = getPlanById(planId)
            if (planResult.isSuccess) {
                val plan = planResult.getOrNull()
                if (plan != null && plan.participants.any { it.uid == user.uid }) {
                    return Result.failure(Exception("El usuario ya está en este plan"))
                }
            }

            // Agregar usuario a la lista de participantes
            plansCollection.document(planId)
                .update("participants", FieldValue.arrayUnion(user))
                .await()

            android.util.Log.d("PlanRepository", "Usuario ${user.uid} se unió al plan $planId")
            Result.success(Unit)
        } catch (e: Exception) {
            android.util.Log.e("PlanRepository", "Error al unirse al plan: ${e.message}", e)
            Result.failure(Exception("Error al unirse al plan: ${e.message}", e))
        }
    }

    /**
     * Remueve un usuario de la lista de participantes de un plan.
     *
     * @param planId ID del plan
     * @param userId UID del usuario a remover
     * @return Result con Unit o un error
     */
    suspend fun leavePlan(planId: String, userId: String): Result<Unit> {
        return try {
            // Obtener el plan actual
            val planResult = getPlanById(planId)
            if (planResult.isFailure) {
                return Result.failure(Exception("No se pudo obtener el plan"))
            }

            val plan = planResult.getOrNull()
            if (plan == null) {
                return Result.failure(Exception("El plan no existe"))
            }

            // Verificar que el usuario esté en el plan
            val participant = plan.participants.find { it.uid == userId }
            if (participant == null) {
                return Result.failure(Exception("El usuario no está en este plan"))
            }

            // Verificar que no sea el creador
            if (plan.createdBy?.uid == userId) {
                return Result.failure(Exception("El creador no puede abandonar el plan. Debe eliminarlo."))
            }

            // Remover usuario de la lista de participantes
            plansCollection.document(planId)
                .update("participants", FieldValue.arrayRemove(participant))
                .await()

            android.util.Log.d("PlanRepository", "Usuario $userId abandonó el plan $planId")
            Result.success(Unit)
        } catch (e: Exception) {
            android.util.Log.e("PlanRepository", "Error al abandonar el plan: ${e.message}", e)
            Result.failure(Exception("Error al abandonar el plan: ${e.message}", e))
        }
    }
}