package it.adr.myscooter.ui.scooterlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.adr.myscooter.data.model.Scooter
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ScooterListViewModel : ViewModel() {

    private val _scooters = MutableStateFlow<List<Scooter>>(emptyList())
    val scooters: StateFlow<List<Scooter>> = _scooters.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Prima dichiara e inizializza _alertMessage
    private val _alertMessage = MutableStateFlow("")
    // Poi dichiara alertMessage usando _alertMessage
    private val _showAlert = MutableStateFlow(false)
    val showAlert: StateFlow<Boolean> = _showAlert.asStateFlow()
    val alertMessage: StateFlow<String> = _alertMessage.asStateFlow()


    init {
        loadScooters()
    }

    private fun loadScooters() {
        viewModelScope.launch {
            _isLoading.value = true
            delay(1500)

            _scooters.value = emptyList()

            _isLoading.value = false
        }
    }

    fun deleteScooter(scooter: Scooter) {
        viewModelScope.launch {
            _isLoading.value = true
            delay(500)

            _scooters.value = _scooters.value.filter { it.id != scooter.id }

            _isLoading.value = false
        }
    }

    fun dismissAlert() {
        _showAlert.value = false
        _alertMessage.value = ""
    }
}