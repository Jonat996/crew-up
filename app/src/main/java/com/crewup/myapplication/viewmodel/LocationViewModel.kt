package com.crewup.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.crewup.myapplication.models.PlaceSuggestion
import kotlinx.coroutines.tasks.await

class LocationViewModel(application: Application) : AndroidViewModel(application) {
    private val placesClient: PlacesClient = Places.createClient(application)
    private val _suggestions = MutableStateFlow<List<PlaceSuggestion>>(emptyList())
    val suggestions: StateFlow<List<PlaceSuggestion>> = _suggestions

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private var userCountry: String? = null
    private var userCity: String? = null

    /**
     * Configura el país y ciudad del usuario para filtrar las búsquedas.
     *
     * @param country Nombre del país del usuario (ej: "Colombia", "México")
     * @param city Ciudad del usuario (ej: "Bogotá", "Ciudad de México")
     */
    fun setUserLocation(country: String?, city: String?) {
        this.userCountry = country
        this.userCity = city
        android.util.Log.d("LocationViewModel", "Usuario ubicado en: $city, $country")
    }

    /**
     * Busca lugares usando Google Places API.
     * Filtra los resultados por el país del usuario si está configurado.
     * Prioriza resultados de la ciudad del usuario.
     *
     * @param query Texto de búsqueda del usuario
     */
    fun searchPlaces(query: String) {
        if (query.isBlank()) {
            _suggestions.value = emptyList()
            return
        }

        _isLoading.value = true
        _error.value = null

        val token = AutocompleteSessionToken.newInstance()
        val requestBuilder = FindAutocompletePredictionsRequest.builder()
            .setQuery(query)
            .setSessionToken(token)

        // Filtrar por país del usuario si está disponible
        val countryCode = getCountryCode(userCountry)
        if (countryCode != null) {
            requestBuilder.setCountries(listOf(countryCode))
            android.util.Log.d("LocationViewModel", "Filtrando búsqueda por país: $countryCode")
        }

        // Si el usuario tiene ciudad configurada, priorizar resultados de esa ciudad
        // agregando la ciudad al query si no está ya incluida
        val city = userCity
        val searchQuery = if (!city.isNullOrBlank() && !query.contains(city, ignoreCase = true)) {
            "$query, $city"
        } else {
            query
        }
        requestBuilder.setQuery(searchQuery)

        val request = requestBuilder.build()

        viewModelScope.launch {
            try {
                val response = placesClient.findAutocompletePredictions(request).await()
                _suggestions.value = response.autocompletePredictions.map { prediction ->
                    PlaceSuggestion(
                        placeId = prediction.placeId,
                        description = prediction.getFullText(null).toString(),
                        secondaryText = prediction.getSecondaryText(null).toString()
                    )
                }
                android.util.Log.d("LocationViewModel", "Encontradas ${_suggestions.value.size} sugerencias")
            } catch (e: Exception) {
                _error.value = "Error en la búsqueda: ${e.message}"
                android.util.Log.e("LocationViewModel", "Error en búsqueda", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearSuggestions() {
        _suggestions.value = emptyList()
    }

    /**
     * Convierte el nombre del país a código ISO 3166-1 alpha-2.
     * Estos códigos son requeridos por Google Places API.
     *
     * @param country Nombre del país
     * @return Código ISO de 2 letras o null si no se encuentra
     */
    private fun getCountryCode(country: String?): String? {
        if (country.isNullOrBlank()) return null

        // Mapeo de países comunes en América Latina a códigos ISO
        return when (country.trim().lowercase()) {
            "colombia" -> "CO"
            "méxico", "mexico" -> "MX"
            "argentina" -> "AR"
            "perú", "peru" -> "PE"
            "chile" -> "CL"
            "venezuela" -> "VE"
            "ecuador" -> "EC"
            "bolivia" -> "BO"
            "paraguay" -> "PY"
            "uruguay" -> "UY"
            "brasil", "brazil" -> "BR"
            "panamá", "panama" -> "PA"
            "costa rica" -> "CR"
            "guatemala" -> "GT"
            "honduras" -> "HN"
            "el salvador" -> "SV"
            "nicaragua" -> "NI"
            "república dominicana", "dominican republic" -> "DO"
            "cuba" -> "CU"
            "puerto rico" -> "PR"
            "españa", "spain" -> "ES"
            "estados unidos", "united states", "usa" -> "US"
            else -> {
                android.util.Log.w("LocationViewModel", "País no encontrado en mapeo: $country")
                null
            }
        }
    }
}