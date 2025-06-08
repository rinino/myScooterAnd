package it.adr.myscooter.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import it.adr.myscooter.data.model.Rifornimento
import kotlinx.coroutines.flow.Flow // Importa Flow per i dati reattivi

/**
 * Data Access Object (DAO) per l'entità Rifornimento.
 * Definisce i metodi per interagire con la tabella 'rifornimenti' nel database.
 */
@Dao
interface RifornimentoDao {

    /**
     * Inserisce un rifornimento nel database.
     * Se un rifornimento con lo stesso ID esiste già, lo sostituisce.
     * @param rifornimento L'oggetto Rifornimento da inserire.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRifornimento(rifornimento: Rifornimento)

    /**
     * Aggiorna un rifornimento esistente nel database.
     * @param rifornimento L'oggetto Rifornimento da aggiornare.
     * @return Il numero di righe aggiornate.
     */
    @Update
    suspend fun updateRifornimento(rifornimento: Rifornimento): Int

    /**
     * Elimina un rifornimento dal database.
     * @param rifornimento L'oggetto Rifornimento da eliminare.
     * @return Il numero di righe eliminate.
     */
    @Delete
    suspend fun deleteRifornimento(rifornimento: Rifornimento): Int

    /**
     * Recupera tutti i rifornimenti dal database, ordinati per data in ordine decrescente.
     * Restituisce un Flow per ricevere aggiornamenti reattivi.
     * @return Un Flow che emette una lista di tutti gli oggetti Rifornimento.
     */
    @Query("SELECT * FROM rifornimenti ORDER BY dataRifornimento DESC")
    fun getAllRifornimenti(): Flow<List<Rifornimento>>

    /**
     * Recupera un rifornimento specifico tramite il suo ID.
     * @param rifornimentoId L'ID del rifornimento da recuperare.
     * @return L'oggetto Rifornimento corrispondente all'ID, o null se non trovato.
     */
    @Query("SELECT * FROM rifornimenti WHERE id = :rifornimentoId LIMIT 1")
    suspend fun getRifornimentoById(rifornimentoId: Long): Rifornimento?

    /**
     * Recupera tutti i rifornimenti associati a un determinato scooter, ordinati per data.
     * Restituisce un Flow per ricevere aggiornamenti reattivi.
     * @param idScooter L'ID dello scooter per cui recuperare i rifornimenti.
     * @return Un Flow che emette una lista di oggetti Rifornimento per lo scooter specificato.
     */
    @Query("SELECT * FROM rifornimenti WHERE idScooter = :idScooter ORDER BY dataRifornimento DESC")
    fun getRifornimentiForScooter(idScooter: Long): Flow<List<Rifornimento>>

    /**
     * Elimina tutti i rifornimenti associati a un determinato scooter.
     * Utile in caso di eliminazione di uno scooter.
     * @param idScooter L'ID dello scooter di cui eliminare i rifornimenti.
     * @return Il numero di righe eliminate.
     */
    @Query("DELETE FROM rifornimenti WHERE idScooter = :idScooter")
    suspend fun deleteRifornimentiForScooter(idScooter: Long): Int

    /**
     * Elimina tutti i rifornimenti dal database.
     * @return Il numero di righe eliminate.
     */
    @Query("DELETE FROM rifornimenti")
    suspend fun deleteAllRifornimenti(): Int
}