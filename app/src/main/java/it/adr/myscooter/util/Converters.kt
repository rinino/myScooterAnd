package it.adr.myscooter.util

import androidx.room.TypeConverter
import java.util.Date

class Converters {
    // Converte un timestamp Long (millisecondi) in un oggetto Date
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    // Converte un oggetto Date in un timestamp Long (millisecondi)
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}