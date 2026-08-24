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
    val history: List<Int> = emptyList()
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
                history = newHistory
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
        if (lowerQuery.isEmpty()) return

        val zone = zones.find {
            it.name.lowercase().contains(lowerQuery) ||
            it.state.lowercase().contains(lowerQuery) ||
            it.quality.lowercase().contains(lowerQuery) ||
            it.block.lowercase().contains(lowerQuery) ||
            it.practice.lowercase().contains(lowerQuery)
        }

        zone?.let {
            updateFrequency((it.min + it.max) / 2)
        }
    }
}
