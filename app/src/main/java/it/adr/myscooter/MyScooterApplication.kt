package it.adr.myscooter

import android.app.Application
import it.adr.myscooter.data.AppDatabase
import it.adr.myscooter.data.repository.ScooterRepository

class MyScooterApplication : Application() {

    // ** Modifica qui: Rendi l'istanza accessibile staticamente **
    companion object {
        lateinit var instance: MyScooterApplication
            private set // Impedisce l'assegnazione esterna, solo all'interno di questa classe
    }

    override fun onCreate() {
        super.onCreate()
        instance = this // Assegna l'istanza corrente quando l'applicazione viene creata
    }

    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
    val repository: ScooterRepository by lazy { ScooterRepository(database.scooterDao()) }
}