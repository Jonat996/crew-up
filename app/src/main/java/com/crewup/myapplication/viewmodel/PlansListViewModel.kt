package com.crewup.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crewup.myapplication.data.repository.PlanRepository
import com.crewup.myapplication.data.repository.UserRepository
import com.crewup.myapplication.models.Plan
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para gestionar listas de planes.
 *
 * Proporciona funcionalidades para obtener planes con filtros,
 * planes del usuario, y planes en los que participa.
 */
class PlansListViewModel(
    private val planRepository: PlanRepository = PlanRepository(),
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {

    private val _plans = MutableStateFlow<List<Plan>>(emptyList())
    val plans: StateFlow<List<Plan>> = _plans.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _currentUserId = MutableStateFlow<String?>(null)
    val currentUserId: StateFlow<String?> = _currentUserId.asStateFlow()

    init {
        loadCurrentUser()
    }

    /**
     * Carga el ID del usuario actual.
     */
    private fun loadCurrentUser() {
        viewModelScope.launch {
            userRepository.getCurrentUser().onSuccess { user ->
                _currentUserId.value = user?.uid
            }
        }
    }

    /**
     * Obtiene todos los planes con filtros opcionales.
     *
     * @param limit Número máximo de planes
     * @param tags Lista de tags para filtrar
     * @param city Ciudad para filtrar
     * @param orderByDate Si true, ordena por fecha
     */
    fun loadPlans(
        limit: Int = 50,
        tags: List<String> = emptyList(),
        city: String? = null,
        orderByDate: Boolean = true
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            planRepository.getPlans(
                limit = limit,
                tags = tags,
                city = city,
                orderByDate = orderByDate
            ).onSuccess { plansList ->
                _plans.value = plansList
                android.util.Log.d("PlansListViewModel", "Planes cargados: ${plansList.size}")
            }.onFailure { e ->
                _error.value = "Error al cargar planes: ${e.message}"
                android.util.Log.e("PlansListViewModel", "Error al cargar planes", e)
            }

            _isLoading.value = false
        }
    }

    /**
     * Obtiene los planes creados por el usuario actual.
     */
    fun loadUserPlans() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            userRepository.getCurrentUser().onSuccess { user ->
                if (user != null) {
                    planRepository.getUserPlans(user.uid).onSuccess { plansList ->
                        _plans.value = plansList
                        android.util.Log.d("PlansListViewModel", "Planes del usuario cargados: ${plansList.size}")
                    }.onFailure { e ->
                        _error.value = "Error al cargar tus planes: ${e.message}"
                        android.util.Log.e("PlansListViewModel", "Error al cargar planes del usuario", e)
                    }
                } else {
                    _error.value = "No se pudo obtener la información del usuario"
                }
            }.onFailure { e ->
                _error.value = "Error al obtener usuario: ${e.message}"
                android.util.Log.e("PlansListViewModel", "Error al obtener usuario", e)
            }

            _isLoading.value = false
        }
    }

    /**
     * Obtiene los planes en los que el usuario está participando.
     */
    fun loadParticipatingPlans() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            userRepository.getCurrentUser().onSuccess { user ->
                if (user != null) {
                    planRepository.getUserParticipatingPlans(user.uid).onSuccess { plansList ->
                        _plans.value = plansList
                        android.util.Log.d("PlansListViewModel", "Planes participantes cargados: ${plansList.size}")
                    }.onFailure { e ->
                        _error.value = "Error al cargar planes en los que participas: ${e.message}"
                        android.util.Log.e("PlansListViewModel", "Error al cargar planes participantes", e)
                    }
                } else {
                    _error.value = "No se pudo obtener la información del usuario"
                }
            }.onFailure { e ->
                _error.value = "Error al obtener usuario: ${e.message}"
                android.util.Log.e("PlansListViewModel", "Error al obtener usuario", e)
            }

            _isLoading.value = false
        }
    }

    /**
     * Obtiene un plan específico por su ID.
     *
     * @param planId ID del plan a obtener
     * @param onSuccess Callback con el plan obtenido
     */
    fun getPlanById(planId: String, onSuccess: (Plan) -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            planRepository.getPlanById(planId).onSuccess { plan ->
                android.util.Log.d("PlansListViewModel", "Plan obtenido: $planId")
                onSuccess(plan)
            }.onFailure { e ->
                _error.value = "Error al obtener el plan: ${e.message}"
                android.util.Log.e("PlansListViewModel", "Error al obtener plan", e)
            }

            _isLoading.value = false
        }
    }

    /**
     * Filtra los planes cargados en memoria por tags.
     *
     * @param tags Lista de tags para filtrar
     */
    fun filterByTags(tags: List<String>) {
        if (tags.isEmpty()) {
            return
        }

        _plans.value = _plans.value.filter { plan ->
            plan.tags.any { tag -> tags.contains(tag) }
        }
    }

    /**
     * Filtra los planes cargados en memoria por ciudad.
     *
     * @param city Ciudad para filtrar
     */
    fun filterByCity(city: String) {
        if (city.isBlank()) {
            return
        }

        _plans.value = _plans.value.filter { plan ->
            plan.location.name.contains(city, ignoreCase = true) ||
            plan.location.fullAddress.contains(city, ignoreCase = true)
        }
    }

    /**
     * Refresca la lista actual de planes.
     * Útil para pull-to-refresh.
     */
    fun refreshPlans() {
        loadPlans()
    }

    /**
     * Limpia la lista de planes.
     */
    fun clearPlans() {
        _plans.value = emptyList()
    }

    /**
     * Limpia el mensaje de error.
     */
    fun clearError() {
        _error.value = null
    }

    /**
     * Verifica si el usuario actual es el creador de un plan.
     *
     * @param plan Plan a verificar
     * @return true si el usuario actual es el creador
     */
    fun isUserCreator(plan: Plan): Boolean {
        return plan.createdBy?.uid == _currentUserId.value
    }

    /**
     * Verifica si el usuario actual está participando en un plan.
     *
     * @param plan Plan a verificar
     * @return true si el usuario está participando
     */
    fun isUserParticipating(plan: Plan): Boolean {
        return plan.participants.any { it.uid == _currentUserId.value }
    }
}
