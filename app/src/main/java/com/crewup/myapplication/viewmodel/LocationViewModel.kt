package com.crewup.myapplication.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.crewup.myapplication.models.PlaceSuggestion
import com.crewup.myapplication.models.PlanLocation
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LocationViewModel(application: Application) : AndroidViewModel(application) {
    private val placesClient: PlacesClient = Places.createClient(application)
    private val _suggestions = MutableStateFlow<List<PlaceSuggestion>>(emptyList())
    val suggestions: StateFlow<List<PlaceSuggestion>> = _suggestions

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun searchPlaces(query: String) {
        if (query.isBlank()) {
            _suggestions.value = emptyList()
            return
        }

        _isLoading.value = true
        _error.value = null

        val token = AutocompleteSessionToken.newInstance()
        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(query)
            .setSessionToken(token)

            .build()

        viewModelScope.launch {
            try {
                val response = placesClient.findAutocompletePredictions(request).await()
                _suggestions.value = response.autocompletePredictions.map { prediction ->
                    PlaceSuggestion(
                        placeId = prediction.placeId,
                        description = prediction.getPrimaryText(null).toString(),
                        secondaryText = prediction.getSecondaryText(null).toString()
                    )
                }
            } catch (e: Exception) {
                _error.value = "Error en la búsqueda: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearSuggestions() {
        _suggestions.value = emptyList()
    }
}