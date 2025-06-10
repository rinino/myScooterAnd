// it.adr.myscooter/MyScooterApplication.kt
package it.adr.myscooter

import android.app.Application
import it.adr.myscooter.data.local.ScooterDatabase // Assicurati che il percorso sia corretto
import it.adr.myscooter.data.repository.ScooterRepository // Assicurati che il percorso sia corretto

class MyScooterApplication : Application() {

    // Dichiarazione lateinit: significa che questa variabile sarà inizializzata più tardi,
    // specificamente nel metodo onCreate.
    // Sarà il tuo contenitore di dipendenze per l'intera app.
    lateinit var scooterRepository: ScooterRepository

    override fun onCreate() {
        super.onCreate()
        // Inizializza il database Room
        val database = ScooterDatabase.getDatabase(this)
        // Inizializza il repository passandogli il DAO dal database
        scooterRepository = ScooterRepository(database.scooterDao())
    }
}