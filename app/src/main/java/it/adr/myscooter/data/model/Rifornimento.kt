package it.adr.myscooter.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "rifornimenti",
    foreignKeys = [
        androidx.room.ForeignKey(
            entity = Scooter::class,
            parentColumns = ["id"],
            childColumns = ["idScooter"],
            onDelete = androidx.room.ForeignKey.CASCADE
        )
    ],
    indices = [
        androidx.room.Index(value = ["idScooter"])
    ]
)
data class Rifornimento(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "idScooter")
    val idScooter: Long,
    val dataRifornimento: Date,
    val kmAttuali: Double,
    val litriBenzina: Double,
    val litriOlio: Double?,
    val kmPercorsi: Double,
    val mediaConsumo: Double?,
    val percentualeOlio: Double?
)