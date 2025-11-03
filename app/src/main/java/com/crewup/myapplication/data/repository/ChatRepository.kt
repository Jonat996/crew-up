package com.crewup.myapplication.data.repository

import com.crewup.myapplication.models.GroupMessage
import com.crewup.myapplication.models.Plan
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID

/**
 * Repositorio para gestionar las operaciones de chat grupal de los planes.
 */
class ChatRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val plansCollection = db.collection("plans")

    /**
     * Envía un mensaje al chat grupal de un plan.
     *
     * @param planId ID del plan
     * @param message El mensaje a enviar
     * @return Result con Unit o un error
     */
    suspend fun sendMessage(planId: String, message: GroupMessage): Result<Unit> {
        return try {
            val messageWithId = message.copy(
                id = UUID.randomUUID().toString(),
                timestamp = Timestamp.now()
            )

            plansCollection.document(planId)
                .update("groupMessages", FieldValue.arrayUnion(messageWithId))
                .await()

            android.util.Log.d("ChatRepository", "Mensaje enviado al plan $planId")
            Result.success(Unit)
        } catch (e: Exception) {
            android.util.Log.e("ChatRepository", "Error al enviar mensaje: ${e.message}", e)
            Result.failure(Exception("Error al enviar el mensaje: ${e.message}", e))
        }
    }

    /**
     * Obtiene los mensajes de un plan en tiempo real.
     *
     * @param planId ID del plan
     * @return Flow con la lista de mensajes ordenados por fecha
     */
    fun getMessagesFlow(planId: String): Flow<List<GroupMessage>> = callbackFlow {
        val subscription = plansCollection.document(planId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    android.util.Log.e("ChatRepository", "Error al escuchar mensajes: ${error.message}", error)
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val plan = snapshot.toObject(Plan::class.java)
                    val messages = plan?.groupMessages?.sortedBy { it.timestamp } ?: emptyList()
                    trySend(messages)
                } else {
                    trySend(emptyList())
                }
            }

        awaitClose {
            subscription.remove()
            android.util.Log.d("ChatRepository", "Listener de mensajes cerrado")
        }
    }

    /**
     * Obtiene los mensajes de un plan de forma estática (sin tiempo real).
     *
     * @param planId ID del plan
     * @return Result con la lista de mensajes o un error
     */
    suspend fun getMessages(planId: String): Result<List<GroupMessage>> {
        return try {
            val snapshot = plansCollection.document(planId).get().await()
            if (snapshot.exists()) {
                val plan = snapshot.toObject(Plan::class.java)
                val messages = plan?.groupMessages?.sortedBy { it.timestamp } ?: emptyList()
                android.util.Log.d("ChatRepository", "Mensajes obtenidos: ${messages.size}")
                Result.success(messages)
            } else {
                Result.failure(Exception("El plan no existe"))
            }
        } catch (e: Exception) {
            android.util.Log.e("ChatRepository", "Error al obtener mensajes: ${e.message}", e)
            Result.failure(Exception("Error al obtener mensajes: ${e.message}", e))
        }
    }

    /**
     * Elimina un mensaje específico del chat grupal.
     * Solo el creador del mensaje o del plan puede eliminar mensajes.
     *
     * @param planId ID del plan
     * @param message El mensaje a eliminar
     * @return Result con Unit o un error
     */
    suspend fun deleteMessage(planId: String, message: GroupMessage): Result<Unit> {
        return try {
            plansCollection.document(planId)
                .update("groupMessages", FieldValue.arrayRemove(message))
                .await()

            android.util.Log.d("ChatRepository", "Mensaje eliminado del plan $planId")
            Result.success(Unit)
        } catch (e: Exception) {
            android.util.Log.e("ChatRepository", "Error al eliminar mensaje: ${e.message}", e)
            Result.failure(Exception("Error al eliminar el mensaje: ${e.message}", e))
        }
    }

    /**
     * Limpia todos los mensajes de un plan.
     * Solo el creador del plan puede hacer esto.
     *
     * @param planId ID del plan
     * @return Result con Unit o un error
     */
    suspend fun clearAllMessages(planId: String): Result<Unit> {
        return try {
            plansCollection.document(planId)
                .update("groupMessages", emptyList<GroupMessage>())
                .await()

            android.util.Log.d("ChatRepository", "Todos los mensajes eliminados del plan $planId")
            Result.success(Unit)
        } catch (e: Exception) {
            android.util.Log.e("ChatRepository", "Error al limpiar mensajes: ${e.message}", e)
            Result.failure(Exception("Error al limpiar mensajes: ${e.message}", e))
        }
    }
}
