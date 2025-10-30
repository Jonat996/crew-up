package com.crewup.myapplication.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crewup.myapplication.data.repository.PlanRepository
import com.crewup.myapplication.data.repository.UserRepository
import com.crewup.myapplication.models.Plan
import com.crewup.myapplication.models.PlanLocation
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para gestionar el flujo de creación de planes.
 *
 * Maneja el estado del plan en construcción y coordina las actualizaciones
 * incrementales en Firestore a medida que el usuario avanza por los pasos.
 */
class CreatePlanViewModel(
    private val planRepository: PlanRepository = PlanRepository(),
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {

    private val _planState = MutableStateFlow(Plan())
    val planState: StateFlow<Plan> = _planState.asStateFlow()

    private val _currentStep = MutableStateFlow(0)
    val currentStep: StateFlow<Int> = _currentStep.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _creationComplete = MutableStateFlow(false)
    val creationComplete: StateFlow<Boolean> = _creationComplete.asStateFlow()

    private val _userCountry = MutableStateFlow<String?>(null)
    val userCountry: StateFlow<String?> = _userCountry.asStateFlow()

    private val _userCity = MutableStateFlow<String?>(null)
    val userCity: StateFlow<String?> = _userCity.asStateFlow()

    init {
        startNewPlan()
        loadUserLocation()
    }

    /**
     * Carga la ubicación del usuario (país y ciudad) para usar en filtros.
     */
    private fun loadUserLocation() {
        viewModelScope.launch {
            userRepository.getCurrentUser().onSuccess { user ->
                if (user != null) {
                    _userCountry.value = user.country
                    _userCity.value = user.city
                    android.util.Log.d("CreatePlanViewModel", "Usuario ubicado en: ${user.city}, ${user.country}")
                }
            }
        }
    }

    /**
     * Inicia un nuevo plan y lo guarda en Firestore con valores iniciales.
     */
    private fun startNewPlan() {
        viewModelScope.launch {
            _isLoading.value = true
            userRepository.getCurrentUser().onSuccess { user ->
                if (user != null) {
                    // Crear objeto PlanUser con la información del creador
                    val creatorInfo = com.crewup.myapplication.models.PlanUser(
                        uid = user.uid,
                        name = user.name,
                        lastName = user.lastName,
                        photoUrl = user.photoUrl,
                        gender = user.gender,
                        occupation = user.occupation,
                        city = user.city
                    )

                    val initialPlan = Plan(
                        creatorUid = user.uid,  // Mantener por compatibilidad
                        createdBy = creatorInfo
                    )
                    val result = planRepository.createPlan(initialPlan)
                    result.onSuccess { planId ->
                        _planState.value = initialPlan.copy(id = planId)
                        android.util.Log.d("CreatePlanViewModel", "Plan iniciado con ID: $planId")
                    }.onFailure { e ->
                        _error.value = "Error al iniciar plan: ${e.message}"
                        android.util.Log.e("CreatePlanViewModel", "Error al iniciar plan", e)
                    }
                } else {
                    _error.value = "No se pudo obtener el usuario autenticado"
                }
            }.onFailure { e ->
                _error.value = "Error al obtener usuario: ${e.message}"
                android.util.Log.e("CreatePlanViewModel", "Error al obtener usuario", e)
            }
            _isLoading.value = false
        }
    }

    /**
     * Actualiza el título y descripción del plan.
     */
    fun updateTitleAndDescription(title: String, description: String) {
        _planState.value = _planState.value.copy(title = title, description = description)
        android.util.Log.d("CreatePlanViewModel", "Título y descripción actualizados")
    }

    /**
     * Actualiza los filtros del plan (tags, rango de edad, género).
     */
    fun updateFilters(tags: List<String>, minAge: Int, maxAge: Int, gender: String = "Todos") {
        _planState.value = _planState.value.copy(
            tags = tags,
            minAge = minAge,
            maxAge = maxAge,
            gender = gender
        )
        android.util.Log.d("CreatePlanViewModel", "Filtros actualizados: $tags, $minAge-$maxAge, $gender")
    }

    /**
     * Actualiza la fecha y hora del plan.
     */
    fun updateDateTime(date: Timestamp?, time: String) {
        _planState.value = _planState.value.copy(date = date, time = time)
        android.util.Log.d("CreatePlanViewModel", "Fecha y hora actualizadas: $date, $time")
    }

    /**
     * Actualiza la ubicación del plan.
     */
    fun updateLocation(location: PlanLocation) {
        _planState.value = _planState.value.copy(location = location)
        android.util.Log.d("CreatePlanViewModel", "Ubicación actualizada: ${location.name}")
    }

    /**
     * Sube una imagen para el plan y actualiza la URL.
     */
    fun uploadImage(imageUri: Uri) {
        viewModelScope.launch {
            _isLoading.value = true
            planRepository.uploadImageAndSaveUrl(_planState.value.id, imageUri).onSuccess { url ->
                _planState.value = _planState.value.copy(imageUrl = url)
                android.util.Log.d("CreatePlanViewModel", "Imagen subida: $url")
            }.onFailure { e ->
                _error.value = "Error al subir imagen: ${e.message}"
                android.util.Log.e("CreatePlanViewModel", "Error al subir imagen", e)
            }
            _isLoading.value = false
        }
    }

    /**
     * Avanza al siguiente paso después de validar el actual.
     */
    fun nextStep() {
        if (validateCurrentStep()) {
            saveCurrentStepToFirestore()
            if (_currentStep.value < 4) {
                _currentStep.value += 1
                android.util.Log.d("CreatePlanViewModel", "Avanzando al paso: ${_currentStep.value}")
            }
        }
    }

    /**
     * Retrocede al paso anterior.
     */
    fun previousStep() {
        if (_currentStep.value > 0) {
            _currentStep.value -= 1
            android.util.Log.d("CreatePlanViewModel", "Retrocediendo al paso: ${_currentStep.value}")
        }
    }

    /**
     * Salta a un paso específico.
     */
    fun goToStep(step: Int) {
        if (step in 0..4) {
            _currentStep.value = step
            android.util.Log.d("CreatePlanViewModel", "Saltando al paso: $step")
        }
    }

    /**
     * Valida el paso actual antes de avanzar.
     */
    private fun validateCurrentStep(): Boolean {
        return when (_currentStep.value) {
            0 -> {
                // Validar título, descripción e imagen
                val valid = _planState.value.title.isNotBlank() &&
                           _planState.value.description.isNotBlank() &&
                           _planState.value.imageUrl.isNotBlank()
                if (!valid) {
                    if (_planState.value.title.isBlank()) {
                        _error.value = "Por favor completa el título"
                    } else if (_planState.value.description.isBlank()) {
                        _error.value = "Por favor completa la descripción"
                    } else {
                        _error.value = "Por favor sube una imagen para tu plan"
                    }
                }
                valid
            }
            1 -> {
                // Validar categorías
                val valid = _planState.value.tags.isNotEmpty()
                if (!valid) {
                    _error.value = "Por favor agrega al menos una categoría"
                }
                valid
            }
            2 -> {
                // Validar fecha y hora
                val valid = _planState.value.date != null && _planState.value.time.isNotBlank()
                if (!valid) {
                    _error.value = "Por favor selecciona la fecha y hora del plan"
                }
                valid
            }
            3 -> {
                // Validar ubicación
                val valid = _planState.value.location.name.isNotBlank()
                if (!valid) {
                    _error.value = "Por favor selecciona una ubicación"
                }
                valid
            }
            else -> true
        }
    }

    /**
     * Guarda el estado actual del plan en Firestore.
     */
    private fun saveCurrentStepToFirestore() {
        viewModelScope.launch {
            val updates = mutableMapOf<String, Any>()

            // Agregar solo campos no vacíos
            if (_planState.value.title.isNotBlank()) {
                updates["title"] = _planState.value.title
            }
            if (_planState.value.description.isNotBlank()) {
                updates["description"] = _planState.value.description
            }
            if (_planState.value.location.name.isNotBlank()) {
                updates["location"] = _planState.value.location
            }
            if (_planState.value.date != null) {
                updates["date"] = _planState.value.date!!
            }
            if (_planState.value.time.isNotBlank()) {
                updates["time"] = _planState.value.time
            }
            if (_planState.value.tags.isNotEmpty()) {
                updates["tags"] = _planState.value.tags
            }
            if (_planState.value.imageUrl.isNotBlank()) {
                updates["imageUrl"] = _planState.value.imageUrl
            }
            updates["minAge"] = _planState.value.minAge
            updates["maxAge"] = _planState.value.maxAge
            updates["gender"] = _planState.value.gender

            if (updates.isNotEmpty()) {
                planRepository.updatePlan(_planState.value.id, updates).onFailure { e ->
                    android.util.Log.e("CreatePlanViewModel", "Error al guardar paso", e)
                }
            }
        }
    }

    /**
     * Finaliza la creación del plan.
     */
    fun finishPlan() {
        viewModelScope.launch {
            _isLoading.value = true
            saveCurrentStepToFirestore()
            _creationComplete.value = true
            _isLoading.value = false
            android.util.Log.d("CreatePlanViewModel", "Plan finalizado: ${_planState.value.id}")
        }
    }

    /**
     * Limpia el mensaje de error.
     */
    fun clearError() {
        _error.value = null
    }
}
