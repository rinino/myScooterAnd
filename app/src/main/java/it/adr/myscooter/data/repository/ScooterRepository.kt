package it.adr.myscooter.data.repository

import it.adr.myscooter.data.dao.ScooterDao
import it.adr.myscooter.data.model.Scooter
import kotlinx.coroutines.flow.Flow

/**
 * Repository per la gestione dei dati degli scooter.
 * Fornisce un'API pulita per l'accesso ai dati al ViewModel, astraggendo la sorgente dati (Room in questo caso).
 */
class ScooterRepository(private val scooterDao: ScooterDao) {


    fun getAllScooters(): Flow<List<Scooter>> {
        return scooterDao.getAllScooters()
    }

    // Inserisce un nuovo scooter nel database
    suspend fun insertScooter(scooter: Scooter) {
        scooterDao.insertScooter(scooter)
    }

    // Aggiorna uno scooter esistente
    suspend fun updateScooter(scooter: Scooter): Int {
        return scooterDao.updateScooter(scooter)
    }

    // Elimina uno scooter
    suspend fun deleteScooter(scooter: Scooter): Int {
        return scooterDao.deleteScooter(scooter)
    }

    // Recupera uno scooter per ID
    suspend fun getScooterById(id: Long): Scooter? {
        return scooterDao.getScooterById(id)
    }

    // Recupera uno scooter per targa
    suspend fun getScooterByTarga(targa: String): Scooter? {
        return scooterDao.getScooterByTarga(targa)
    }

    // Elimina tutti gli scooter
    suspend fun deleteAllScooters(): Int {
        return scooterDao.deleteAllScooters()
    }

    // TODO: Aggiungi qui le funzioni per gestire le immagini localmente
    // La gestione delle immagini richiede un'implementazione separata in Android,
    // simile a come hai fatto in Swift con FileManager.
    // Per ora ci concentriamo sulla parte del database.
}