package com.crewup.myapplication.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crewup.myapplication.data.repository.PlanRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlanViewModel(private val repository: PlanRepository = PlanRepository()) : ViewModel() {
    private val _imageUrl = MutableStateFlow<String?>(null)
    val imageUrl: StateFlow<String?> = _imageUrl

    private val _uploadState = MutableStateFlow<UploadState>(UploadState.Idle)
    val uploadState: StateFlow<UploadState> = _uploadState

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
}

sealed class UploadState {
    object Idle : UploadState()
    object Loading : UploadState()
    object Success : UploadState()
    data class Error(val message: String) : UploadState()
}