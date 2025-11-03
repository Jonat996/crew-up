package com.crewup.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crewup.myapplication.data.repository.ChatRepository
import com.crewup.myapplication.data.repository.PlanRepository
import com.crewup.myapplication.data.repository.UserRepository
import com.crewup.myapplication.models.GroupMessage
import com.crewup.myapplication.models.Plan
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para gestionar el chat grupal de un plan.
 */
class GroupChatViewModel(
    private val chatRepository: ChatRepository = ChatRepository(),
    private val planRepository: PlanRepository = PlanRepository(),
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {

    private val _plan = MutableStateFlow<Plan?>(null)
    val plan: StateFlow<Plan?> = _plan.asStateFlow()

    private val _messages = MutableStateFlow<List<GroupMessage>>(emptyList())
    val messages: StateFlow<List<GroupMessage>> = _messages.asStateFlow()

    private val _currentUserId = MutableStateFlow<String?>(null)
    val currentUserId: StateFlow<String?> = _currentUserId.asStateFlow()

    private val _messageText = MutableStateFlow("")
    val messageText: StateFlow<String> = _messageText.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadCurrentUser()
    }

    /**
     * Carga la información del usuario actual.
     */
    private fun loadCurrentUser() {
        viewModelScope.launch {
            userRepository.getCurrentUser().onSuccess { user ->
                _currentUserId.value = user?.uid
            }
        }
    }

    /**
     * Inicializa el chat cargando el plan y escuchando los mensajes en tiempo real.
     *
     * @param planId ID del plan
     */
    fun initializeChat(planId: String) {
        loadPlan(planId)
        listenToMessages(planId)
    }

    /**
     * Carga la información del plan.
     */
    private fun loadPlan(planId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            planRepository.getPlanById(planId).onSuccess { plan ->
                _plan.value = plan
            }.onFailure { e ->
                _error.value = "Error al cargar el plan: ${e.message}"
                android.util.Log.e("GroupChatViewModel", "Error al cargar plan", e)
            }
            _isLoading.value = false
        }
    }

    /**
     * Escucha los mensajes del plan en tiempo real.
     */
    private fun listenToMessages(planId: String) {
        viewModelScope.launch {
            chatRepository.getMessagesFlow(planId).collect { messages ->
                _messages.value = messages
                android.util.Log.d("GroupChatViewModel", "Mensajes actualizados: ${messages.size}")
            }
        }
    }

    /**
     * Actualiza el texto del mensaje que el usuario está escribiendo.
     */
    fun updateMessageText(text: String) {
        _messageText.value = text
    }

    /**
     * Envía un mensaje al chat grupal.
     */
    fun sendMessage() {
        val text = _messageText.value.trim()
        if (text.isBlank()) {
            _error.value = "El mensaje no puede estar vacío"
            return
        }

        val planId = _plan.value?.id
        if (planId == null) {
            _error.value = "Error: No se pudo identificar el plan"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true

            userRepository.getCurrentUser().onSuccess { user ->
                if (user != null) {
                    val message = GroupMessage(
                        userId = user.uid,
                        userName = "${user.name} ${user.lastName}",
                        userPhotoUrl = user.photoUrl,
                        message = text
                    )

                    chatRepository.sendMessage(planId, message).onSuccess {
                        _messageText.value = "" // Limpiar el campo de texto
                        android.util.Log.d("GroupChatViewModel", "Mensaje enviado exitosamente")
                    }.onFailure { e ->
                        _error.value = "Error al enviar mensaje: ${e.message}"
                        android.util.Log.e("GroupChatViewModel", "Error al enviar mensaje", e)
                    }
                } else {
                    _error.value = "No se pudo obtener la información del usuario"
                }
            }.onFailure { e ->
                _error.value = "Error al obtener usuario: ${e.message}"
                android.util.Log.e("GroupChatViewModel", "Error al obtener usuario", e)
            }

            _isLoading.value = false
        }
    }

    /**
     * Elimina un mensaje del chat.
     * Solo el creador del mensaje puede eliminarlo.
     */
    fun deleteMessage(message: GroupMessage) {
        val planId = _plan.value?.id
        if (planId == null) {
            _error.value = "Error: No se pudo identificar el plan"
            return
        }

        // Verificar que el usuario actual sea el creador del mensaje
        if (message.userId != _currentUserId.value) {
            _error.value = "Solo puedes eliminar tus propios mensajes"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            chatRepository.deleteMessage(planId, message).onSuccess {
                android.util.Log.d("GroupChatViewModel", "Mensaje eliminado exitosamente")
            }.onFailure { e ->
                _error.value = "Error al eliminar mensaje: ${e.message}"
                android.util.Log.e("GroupChatViewModel", "Error al eliminar mensaje", e)
            }
            _isLoading.value = false
        }
    }

    /**
     * Limpia el mensaje de error.
     */
    fun clearError() {
        _error.value = null
    }

    /**
     * Verifica si el usuario actual es el creador del plan.
     */
    fun isCurrentUserCreator(): Boolean {
        return _plan.value?.createdBy?.uid == _currentUserId.value
    }

    /**
     * Verifica si el usuario actual es un participante del plan.
     */
    fun isCurrentUserParticipant(): Boolean {
        val currentUser = _currentUserId.value ?: return false
        return _plan.value?.participants?.any { it.uid == currentUser } ?: false
    }
}
