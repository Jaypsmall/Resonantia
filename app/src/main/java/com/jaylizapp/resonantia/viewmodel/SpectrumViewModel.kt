package com.jaylizapp.resonantia.viewmodel

import androidx.lifecycle.ViewModel
import com.jaylizapp.resonantia.data.Zone
import com.jaylizapp.resonantia.data.zones
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.abs

data class SpectrumState(
    val currentFrequency: Int = 500,
    val targetFrequency: Int = 850,
    val history: List<Int> = emptyList(),
    val searchResult: Zone? = null,
    val lastSearchQuery: String = ""
)

class SpectrumViewModel : ViewModel() {
    private val _state = MutableStateFlow(SpectrumState())
    val state: StateFlow<SpectrumState> = _state.asStateFlow()

    fun updateFrequency(frequency: Int) {
        val newFreq = frequency.coerceIn(100, 1000)
        if (newFreq != _state.value.currentFrequency) {
            val newHistory = (_state.value.history + newFreq).takeLast(15)
            _state.value = _state.value.copy(
                currentFrequency = newFreq,
                history = newHistory,
                searchResult = null // Limpiamos resultado al movernos manualmente
            )
        }
    }

    fun updateTargetFrequency(frequency: Int) {
        _state.value = _state.value.copy(targetFrequency = frequency.coerceIn(100, 1000))
    }

    fun clearHistory() {
        _state.value = _state.value.copy(history = emptyList())
    }

    fun getZoneForFrequency(frequency: Int): Zone {
        return zones.find { frequency in it.min..it.max } ?: zones.last()
    }

    fun searchState(query: String) {
        val lowerQuery = query.lowercase().trim()
        if (lowerQuery.isEmpty()) {
            _state.value = _state.value.copy(searchResult = null, lastSearchQuery = "")
            return
        }

        val zone = zones.find { z ->
            z.name.lowercase().contains(lowerQuery) ||
            z.state.lowercase().contains(lowerQuery) ||
            z.keywords.any { it.lowercase().contains(lowerQuery) } ||
            z.quality.lowercase().contains(lowerQuery) ||
            z.description.lowercase().contains(lowerQuery)
        }

        _state.value = _state.value.copy(
            searchResult = zone,
            lastSearchQuery = query
        )

        zone?.let {
            // Opcional: Podríamos actualizar la frecuencia automáticamente o no.
            // Para que el usuario vea la descripción primero, quizás mejor solo mostrar el resultado.
            // Pero el usuario pidió "a que frecuencia corresponde", así que lo llevamos allí.
            updateFrequency((it.min + it.max) / 2)
        }
    }
    
    fun clearSearch() {
        _state.value = _state.value.copy(searchResult = null, lastSearchQuery = "")
    }
}
