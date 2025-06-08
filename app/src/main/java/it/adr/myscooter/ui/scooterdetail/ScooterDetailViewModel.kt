package it.adr.myscooter.ui.scooterdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.adr.myscooter.data.model.Scooter
import it.adr.myscooter.data.repository.ScooterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ScooterDetailViewModel(
    private val scooterRepository: ScooterRepository,
    savedStateHandle: SavedStateHandle // Permette di accedere agli argomenti della navigazione
) : ViewModel() {

    // Recupera lo scooterId dagli argomenti della navigazione
    private val scooterId: Long? = savedStateHandle["scooterId"]

    // Stato che contiene lo scooter attualmente visualizzato o null se non caricato
    private val _scooter = MutableStateFlow<Scooter?>(null)
    val scooter: StateFlow<Scooter?> = _scooter.asStateFlow()

    // Stato per gestire l'indicatore di caricamento
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Stato per gli alert (es. errore di caricamento)
    private val _showAlert = MutableStateFlow(false)
    val showAlert: StateFlow<Boolean> = _showAlert.asStateFlow()
    private val _alertMessage = MutableStateFlow("")
    val alertMessage: StateFlow<String> = _alertMessage.asStateFlow()

    init {
        // Carica lo scooter non appena il ViewModel viene creato
        scooterId?.let {
            loadScooter(it)
        } ?: run {
            // Se l'ID è nullo, significa che c'è stato un errore di navigazione
            _alertMessage.value = "Errore: ID scooter non trovato."
            _showAlert.value = true
        }
    }

    private fun loadScooter(id: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Chiamata al repository per ottenere lo scooter per ID
                val loadedScooter = scooterRepository.getScooterById(id)
                _scooter.value = loadedScooter
            } catch (e: Exception) {
                _alertMessage.value = "Errore durante il caricamento dello scooter: ${e.message}"
                _showAlert.value = true
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Funzione per aggiornare lo scooter nel database
    fun updateScooter(scooter: Scooter) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                scooterRepository.updateScooter(scooter)
                _scooter.value = scooter // Aggiorna lo stato locale dopo il salvataggio
                // Potresti aggiungere un messaggio di successo
            } catch (e: Exception) {
                _alertMessage.value = "Errore durante l'aggiornamento dello scooter: ${e.message}"
                _showAlert.value = true
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun showAlert(message: String) {
        _alertMessage.value = message
        _showAlert.value = true
    }

    // Funzione per chiudere l'alert (già presente)
    fun dismissAlert() {
        _showAlert.value = false
        _alertMessage.value = ""
    }

    // Qui avrai bisogno di una Factory per il ViewModel che passi il repository
    // Questo è un placeholder, l'implementazione della factory è più complessa
    // e spesso si usa Hilt/Dagger o un Provider manuale per i ViewModel con dipendenze.
    // Per ora, ti servirà una configurazione in MainActivity.kt per fornire il repository.
}