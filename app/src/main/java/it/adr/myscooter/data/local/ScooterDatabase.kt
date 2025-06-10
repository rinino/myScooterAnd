// it.adr.myscooter.data.local/ScooterDatabase.kt
package it.adr.myscooter.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import it.adr.myscooter.data.dao.ScooterDao
import it.adr.myscooter.data.model.Scooter

@Database(entities = [Scooter::class], version = 1, exportSchema = false)
abstract class ScooterDatabase : RoomDatabase() {

    abstract fun scooterDao(): ScooterDao

    companion object {
        @Volatile // Garantisce che l'istanza sia sempre aggiornata tra i thread
        private var Instance: ScooterDatabase? = null

        fun getDatabase(context: Context): ScooterDatabase {
            // Se l'istanza non è null, restituiscila, altrimenti crea il database
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context.applicationContext, ScooterDatabase::class.java, "scooter_database")
                    .fallbackToDestructiveMigration() // Distrugge e ricrea il DB se lo schema cambia (utile in fase di dev)
                    .build()
                    .also { Instance = it } // Assegna l'istanza appena creata
            }
        }
    }
}