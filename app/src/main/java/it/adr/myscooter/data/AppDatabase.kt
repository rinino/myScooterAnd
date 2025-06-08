// myScooter/app/src/main/java/it/adr/myscooter/data/AppDatabase.kt
package it.adr.myscooter.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import it.adr.myscooter.data.dao.ScooterDao
import it.adr.myscooter.data.dao.RifornimentoDao
import it.adr.myscooter.data.model.Scooter
import it.adr.myscooter.data.model.Rifornimento
import it.adr.myscooter.util.Converters

/**
 * Classe astratta del database Room per l'applicazione myScooter.
 * Definisce le entità (tabelle) del database e i DAO (Data Access Objects) per interagire con esse.
 * Contiene anche l'istanza singleton del database per garantire un accesso thread-safe.
 */
@Database(
    entities = [Scooter::class, Rifornimento::class],
    version = 1, // La versione del database. Incrementa per gestire le migrazioni dello schema.
    exportSchema = false
)

@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {


    abstract fun scooterDao(): ScooterDao
    abstract fun rifornimentoDao(): RifornimentoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Ottiene l'istanza singleton del database.
         * Se l'istanza non esiste, la crea in modo thread-safe.
         * @param context Il contesto dell'applicazione.
         * @return L'istanza di AppDatabase.
         */
        fun getDatabase(context: Context): AppDatabase {
            // Restituisce l'istanza esistente se non è nulla, altrimenti crea una nuova istanza.
            return INSTANCE ?: synchronized(this) {
                // Crea l'istanza del database.
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "myscooter_database"
                )
                    .build()
                INSTANCE = instance // Assegna l'istanza appena creata
                instance // Restituisce la nuova istanza
            }
        }
    }
}