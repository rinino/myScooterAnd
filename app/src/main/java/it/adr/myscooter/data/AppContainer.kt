// it.adr.myscooter.data/AppContainer.kt
package it.adr.myscooter.data

import android.content.Context
import it.adr.myscooter.data.dao.ScooterDao
import it.adr.myscooter.data.local.ScooterDatabase
import it.adr.myscooter.data.repository.ScooterRepository

interface AppContainer {
    val scooterRepository: ScooterRepository
}

class AppDataContainer(private val context: Context) : AppContainer {

    // Lazily initialize ScooterDatabase
    private val scooterDatabase: ScooterDatabase by lazy {
        ScooterDatabase.getDatabase(context)
    }

    // Lazily initialize ScooterDao
    private val scooterDao: ScooterDao by lazy {
        scooterDatabase.scooterDao()
    }

    // Lazily initialize ScooterRepository
    override val scooterRepository: ScooterRepository by lazy {
        ScooterRepository(scooterDao)
    }
}