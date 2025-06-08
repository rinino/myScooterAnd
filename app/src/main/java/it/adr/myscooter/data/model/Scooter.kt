package it.adr.myscooter.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "scooters",
    indices = [Index(value = ["targa"], unique = true)]
)
data class Scooter(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val marca: String,
    val modello: String,
    val cilindrata: Int,
    val anno: Int,
    val targa: String,
    val miscelatore: Boolean,
    val imgName: String?
)