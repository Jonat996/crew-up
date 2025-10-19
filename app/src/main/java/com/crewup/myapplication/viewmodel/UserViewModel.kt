package com.crewup.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crewup.myapplication.data.repository.UserRepository
import com.crewup.myapplication.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class UserState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null
)

class UserViewModel(private val repository: UserRepository = UserRepository()) : ViewModel() {

    private val _userState = MutableStateFlow(UserState())
    val userState: StateFlow<UserState> = _userState

    init {
        println("=== UserViewModel inicializado ===")
        loadUser()
    }

    fun loadUser() {
        println("=== loadUser() llamado ===")
        viewModelScope.launch {
            _userState.value = _userState.value.copy(isLoading = true, error = null)
            println("Estado isLoading = true")

            val result = repository.getCurrentUser()
            println("Resultado de getCurrentUser: ${result.isSuccess}, data: ${result.getOrNull()}")

            if (result.isSuccess) {
                val user = result.getOrNull()
                println("Usuario cargado exitosamente: $user")
                _userState.value = _userState.value.copy(isLoading = false, user = user)
            } else {
                val error = result.exceptionOrNull()?.message
                println("Error al cargar usuario: $error")
                _userState.value = _userState.value.copy(isLoading = false, error = error)
            }
        }
    }

    // Función genérica para actualizar cualquier campo
    fun updateUserField(field: String, value: Any) {
        viewModelScope.launch {
            _userState.value = _userState.value.copy(isLoading = true, error = null)
            val updates = mapOf(field to value)
            val result = repository.updateUserProfile(updates)
            if (result.isSuccess) {
                // Recargar usuario actualizado
                loadUser()
            } else {
                _userState.value = _userState.value.copy(isLoading = false, error = result.exceptionOrNull()?.message)
            }
        }
    }

    // Actualizar múltiples campos a la vez (incluyendo Firebase Auth)
    fun updateUserProfile(
        name: String? = null,
        lastName: String? = null,
        email: String? = null,
        occupation: String? = null,
        gender: String? = null,
        city: String? = null,
        country: String? = null,
        phoneNumber: String? = null
    ) {
        viewModelScope.launch {
            _userState.value = _userState.value.copy(isLoading = true, error = null)

            val updates = mutableMapOf<String, Any>()
            name?.let { updates["name"] = it }
            lastName?.let { updates["lastName"] = it }
            email?.let { updates["email"] = it }
            occupation?.let { updates["occupation"] = it }
            gender?.let { updates["gender"] = it }
            city?.let { updates["city"] = it }
            country?.let { updates["country"] = it }
            phoneNumber?.let { updates["phoneNumber"] = it }

            // Actualizar Firebase Auth si es necesario
            val authUpdateNeeded = name != null || lastName != null || email != null
            if (authUpdateNeeded) {
                val displayName = if (name != null || lastName != null) {
                    val currentName = name ?: _userState.value.user?.name ?: ""
                    val currentLastName = lastName ?: _userState.value.user?.lastName ?: ""
                    "$currentName $currentLastName".trim()
                } else null

                val authResult = repository.updateFirebaseAuth(
                    displayName = displayName,
                    email = email
                )

                if (authResult.isFailure) {
                    _userState.value = _userState.value.copy(
                        isLoading = false,
                        error = authResult.exceptionOrNull()?.message
                    )
                    return@launch
                }
            }

            // Actualizar Firestore
            val result = repository.updateUserProfile(updates)
            if (result.isSuccess) {
                loadUser()
            } else {
                _userState.value = _userState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message
                )
            }
        }
    }

    // Ejemplos específicos (usa updateUserField internamente)
    fun updateOccupation(occupation: String) {
        updateUserField("occupation", occupation)
    }

    fun updateGender(gender: String) {
        updateUserField("gender", gender)
    }

    fun updateCity(city: String) {
        updateUserField("city", city)
    }

    // Otros campos: updateName, updateLastName, etc., similarmente

    fun clearError() {
        _userState.value = _userState.value.copy(error = null)
    }
}
