// myScooter/app/src/main/java/it/adr/myscooter/ui/scooterlist/ScooterListViewModel.kt
package it.adr.myscooter.ui.scooterlist

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import it.adr.myscooter.data.AppDatabase
import it.adr.myscooter.data.model.Scooter
import it.adr.myscooter.data.repository.ScooterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
/**
 * ViewModel per la ScooterListScreen.
 * Gestisce la logica di business e lo stato UI per la lista degli scooter.
 */
class ScooterListViewModel(application: Application) : AndroidViewModel(application) {

    private val scooterRepository: ScooterRepository

    // Stato per la lista degli scooter (osservato dalla UI)
    private val _scooters = MutableStateFlow<List<Scooter>>(emptyList())
    val scooters: StateFlow<List<Scooter>> = _scooters.asStateFlow()

    // Stato per l'indicatore di caricamento
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Stato per mostrare alert/messaggi all'utente
    private val _showAlert = MutableStateFlow(false)
    val showAlert: StateFlow<Boolean> = _showAlert.asStateFlow()

    private val _alertMessage = MutableStateFlow("")
    val alertMessage: StateFlow<String> = _alertMessage.asStateFlow()

    init {
        // Inizializza il repository con il DAO dal database singleton
        val scooterDao = AppDatabase.getDatabase(application).scooterDao()
        scooterRepository = ScooterRepository(scooterDao)
        loadScooters() // Carica gli scooter all'avvio del ViewModel
    }

    fun loadScooters() {
        viewModelScope.launch {
            _isLoading.value = true
            delay(1000) // Simula un ritardo di caricamento (come DispatchQueue.main.asyncAfter)

            // Raccogli i dati dal Flow del repository
            scooterRepository.getAllScooters().collect { loadedScooters ->
                _scooters.value = loadedScooters
                _isLoading.value = false
            }
        }
    }

    fun deleteScooter(scooter: Scooter) {
        viewModelScope.launch {
            val deletedRows = scooterRepository.deleteScooter(scooter)
            if (deletedRows > 0) {
                // Se la cancellazione ha successo, la UI si aggiornerà automaticamente grazie a Flow
                // TODO: Aggiungi qui la logica per eliminare l'immagine dal filesystem
                Log.d("ScooterListViewModel", "Scooter ${scooter.id} eliminato con successo.")
            } else {
                showAlertWithMessage("Errore durante l'eliminazione dello scooter: ${scooter.targa}")
            }
        }
    }

    fun saveNewScooter(newScooter: Scooter) {
        viewModelScope.launch {
            try {
                scooterRepository.insertScooter(newScooter)
                showAlertWithMessage("Scooter inserito con successo!")
                // La UI si aggiornerà automaticamente grazie al Flow
                // reset dei campi del form verrà gestito nell'AddNewScooterFormView o qui
            } catch (e: Exception) {
                showAlertWithMessage("Errore durante il salvataggio dello scooter: ${e.localizedMessage}")
                Log.e("ScooterListViewModel", "Errore salvataggio scooter", e)
            }
        }
    }

    fun showAlertWithMessage(message: String) {
        _alertMessage.value = message
        _showAlert.value = true
    }

    fun dismissAlert() {
        _showAlert.value = false
        _alertMessage.value = ""
    }

    // TODO: La gestione della validazione dei campi del form e il reset
    // possono essere centralizzati qui o gestiti direttamente nel Composable del form
    // per semplicità, per ora lascio la logica di validazione nella UI come nel tuo Swift,
    // ma la gestione dei valori la porteremo qui se il form è integrato direttamente.
}