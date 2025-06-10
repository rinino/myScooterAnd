// it.adr.myscooter.ui.scooterlist/ScooterListViewModel.kt
package it.adr.myscooter.ui.scooterlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.adr.myscooter.data.model.Scooter
import it.adr.myscooter.data.repository.ScooterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ScooterListViewModel(private val scooterRepository: ScooterRepository) : ViewModel() {

    private val _scooters = MutableStateFlow<List<Scooter>>(emptyList())
    val scooters: StateFlow<List<Scooter>> = _scooters

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _showAlert = MutableStateFlow(false)
    val showAlert: StateFlow<Boolean> = _showAlert

    private val _alertMessage = MutableStateFlow("")
    val alertMessage: StateFlow<String> = _alertMessage

    init {
        // Avvia la raccolta degli scooter all'inizializzazione del ViewModel
        viewModelScope.launch {
            _isLoading.value = true // Indica che il caricamento è iniziato
            try {
                scooterRepository.getAllScooters().collect { fetchedScooters ->
                    _scooters.value = fetchedScooters
                    _isLoading.value = false // Caricamento completato
                }
            } catch (e: Exception) {
                // Gestione errori, es. mostrare un messaggio all'utente
                _isLoading.value = false
                _showAlert.value = true
                _alertMessage.value = "Errore durante il caricamento degli scooter: ${e.localizedMessage}"
            }
        }
    }

    fun deleteScooter(scooter: Scooter) {
        viewModelScope.launch {
            try {
                val deletedRows = scooterRepository.deleteScooter(scooter)
                if (deletedRows > 0) {
                    _showAlert.value = true
                    _alertMessage.value = "Scooter '${scooter.targa}' eliminato con successo!"
                } else {
                    _showAlert.value = true
                    _alertMessage.value = "Impossibile eliminare lo scooter '${scooter.targa}'."
                }
            } catch (e: Exception) {
                _showAlert.value = true
                _alertMessage.value = "Errore durante l'eliminazione: ${e.localizedMessage}"
            }
        }
    }

    fun dismissAlert() {
        _showAlert.value = false
        _alertMessage.value = ""
    }

    // Aggiungi qui altre funzioni del ViewModel (es. addScooter, updateScooter)
}