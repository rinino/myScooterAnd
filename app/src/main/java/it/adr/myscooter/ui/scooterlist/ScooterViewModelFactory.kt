// it.adr.myscooter.ui.scooterlist/ScooterViewModelFactory.kt
package it.adr.myscooter.ui.scooterlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import it.adr.myscooter.data.repository.ScooterRepository

class ScooterViewModelFactory(private val repository: ScooterRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Verifica che la classe del ViewModel richiesta sia proprio ScooterListViewModel
        if (modelClass.isAssignableFrom(ScooterListViewModel::class.java)) {
            // Restituisci una nuova istanza di ScooterListViewModel, passandogli il repository
            return ScooterListViewModel(repository) as T
        }
        // Se la factory è stata usata per un altro ViewModel, lancia un errore
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}