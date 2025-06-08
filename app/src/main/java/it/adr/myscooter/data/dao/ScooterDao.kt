package it.adr.myscooter.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import it.adr.myscooter.data.model.Scooter
import kotlinx.coroutines.flow.Flow // Importa Flow per i dati reattivi

/**
 * Data Access Object (DAO) per l'entità Scooter.
 * Definisce i metodi per interagire con la tabella 'scooters' nel database.
 */
@Dao
interface ScooterDao {

    /**
     * Inserisce uno o più scooter nel database.
     * Se uno scooter con lo stesso ID esiste già, lo sostituisce.
     * @param scooter L'oggetto Scooter da inserire.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScooter(scooter: Scooter)

    /**
     * Inserisce una lista di scooter nel database.
     * Se uno scooter con lo stesso ID esiste già, lo sostituisce.
     * @param scooters La lista di oggetti Scooter da inserire.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllScooters(scooters: List<Scooter>)

    /**
     * Aggiorna uno scooter esistente nel database.
     * @param scooter L'oggetto Scooter da aggiornare.
     * @return Il numero di righe aggiornate.
     */
    @Update
    suspend fun updateScooter(scooter: Scooter): Int

    /**
     * Elimina uno scooter dal database.
     * @param scooter L'oggetto Scooter da eliminare.
     * @return Il numero di righe eliminate.
     */
    @Delete
    suspend fun deleteScooter(scooter: Scooter): Int

    /**
     * Recupera tutti gli scooter dal database.
     * Restituisce un Flow per ricevere aggiornamenti reattivi quando i dati cambiano.
     * @return Un Flow che emette una lista di tutti gli oggetti Scooter.
     */
    @Query("SELECT * FROM scooters ORDER BY marca, modello")
    fun getAllScooters(): Flow<List<Scooter>>

    /**
     * Recupera uno scooter specifico tramite il suo ID.
     * @param scooterId L'ID dello scooter da recuperare.
     * @return L'oggetto Scooter corrispondente all'ID, o null se non trovato.
     */
    @Query("SELECT * FROM scooters WHERE id = :scooterId LIMIT 1")
    suspend fun getScooterById(scooterId: Long): Scooter?

    /**
     * Recupera uno scooter specifico tramite la sua targa (che è un campo unico).
     * @param targa La targa dello scooter da recuperare.
     * @return L'oggetto Scooter corrispondente alla targa, o null se non trovato.
     */
    @Query("SELECT * FROM scooters WHERE targa = :targa LIMIT 1")
    suspend fun getScooterByTarga(targa: String): Scooter?

    /**
     * Elimina tutti gli scooter dal database.
     * @return Il numero di righe eliminate.
     */
    @Query("DELETE FROM scooters")
    suspend fun deleteAllScooters(): Int
}