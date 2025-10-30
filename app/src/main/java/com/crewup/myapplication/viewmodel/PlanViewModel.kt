package com.crewup.myapplication.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crewup.myapplication.data.repository.PlanRepository
import com.crewup.myapplication.data.repository.UserRepository
import com.crewup.myapplication.models.PlanUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlanViewModel(
    private val repository: PlanRepository = PlanRepository(),
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {
    private val _imageUrl = MutableStateFlow<String?>(null)
    val imageUrl: StateFlow<String?> = _imageUrl

    private val _uploadState = MutableStateFlow<UploadState>(UploadState.Idle)
    val uploadState: StateFlow<UploadState> = _uploadState

    private val _deleteState = MutableStateFlow<ActionState>(ActionState.Idle)
    val deleteState: StateFlow<ActionState> = _deleteState

    private val _joinState = MutableStateFlow<ActionState>(ActionState.Idle)
    val joinState: StateFlow<ActionState> = _joinState

    private val _leaveState = MutableStateFlow<ActionState>(ActionState.Idle)
    val leaveState: StateFlow<ActionState> = _leaveState

    fun uploadImage(planId: String, imageUri: Uri) {
        viewModelScope.launch {
            _uploadState.value = UploadState.Loading
            val result = repository.uploadImageAndSaveUrl(planId, imageUri)
            if (result.isSuccess) {
                _imageUrl.value = result.getOrNull()
                _uploadState.value = UploadState.Success
            } else {
                _uploadState.value = UploadState.Error(result.exceptionOrNull()?.message ?: "Error desconocido")
            }
        }
    }

    fun deletePlan(planId: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _deleteState.value = ActionState.Loading
            val result = repository.deletePlan(planId)
            if (result.isSuccess) {
                _deleteState.value = ActionState.Success
                onSuccess()
            } else {
                _deleteState.value = ActionState.Error(result.exceptionOrNull()?.message ?: "Error al eliminar el plan")
            }
        }
    }

    fun joinPlan(planId: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _joinState.value = ActionState.Loading

            // Obtener el usuario actual para agregarlo al plan
            userRepository.getCurrentUser().onSuccess { user ->
                if (user != null) {
                    val planUser = PlanUser(
                        uid = user.uid,
                        name = "${user.name} ${user.lastName}".trim(),
                        photoUrl = user.photoUrl
                    )

                    repository.joinPlan(planId, planUser).onSuccess {
                        _joinState.value = ActionState.Success
                        onSuccess()
                    }.onFailure { e ->
                        _joinState.value = ActionState.Error(e.message ?: "Error al unirse al plan")
                    }
                } else {
                    _joinState.value = ActionState.Error("No se pudo obtener la información del usuario")
                }
            }.onFailure { e ->
                _joinState.value = ActionState.Error(e.message ?: "Error al obtener usuario")
            }
        }
    }

    fun leavePlan(planId: String, userId: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _leaveState.value = ActionState.Loading
            val result = repository.leavePlan(planId, userId)
            if (result.isSuccess) {
                _leaveState.value = ActionState.Success
                onSuccess()
            } else {
                _leaveState.value = ActionState.Error(result.exceptionOrNull()?.message ?: "Error al salir del plan")
            }
        }
    }

    fun resetDeleteState() {
        _deleteState.value = ActionState.Idle
    }

    fun resetJoinState() {
        _joinState.value = ActionState.Idle
    }

    fun resetLeaveState() {
        _leaveState.value = ActionState.Idle
    }
}

sealed class UploadState {
    object Idle : UploadState()
    object Loading : UploadState()
    object Success : UploadState()
    data class Error(val message: String) : UploadState()
}

sealed class ActionState {
    object Idle : ActionState()
    object Loading : ActionState()
    object Success : ActionState()
    data class Error(val message: String) : ActionState()
}