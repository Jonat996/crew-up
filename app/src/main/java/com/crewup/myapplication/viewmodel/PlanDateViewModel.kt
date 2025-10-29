package com.crewup.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate
import java.time.LocalTime

class PlanDateViewModel : ViewModel() {
    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate

    private val _selectedTime = MutableStateFlow(LocalTime.now())
    val selectedTime: StateFlow<LocalTime> = _selectedTime

    fun updateSelectedDate(newDate: LocalDate) {
        if (newDate.isAfter(LocalDate.now().minusDays(1))) {  // Evita fechas pasadas
            _selectedDate.value = newDate
        }
    }

    fun updateSelectedTime(newTime: LocalTime) {
        _selectedTime.value = newTime
    }

    // Metodo para obtener la fecha y hora combinadas
    fun getSelectedDateTime(): String = "${_selectedDate.value} ${_selectedTime.value}"
}