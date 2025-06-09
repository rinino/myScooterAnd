package it.adr.myscooter.data.model

import android.net.Uri // Importa Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scooters")
data class Scooter(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val marca: String,
    val modello: String,
    val cilindrata: Int,
    val targa: String,
    val anno: Int,
    val miscelatore: Boolean,
    val imgName: String? // Cambiato da Int? a String? per l'URI dell'immagine
)