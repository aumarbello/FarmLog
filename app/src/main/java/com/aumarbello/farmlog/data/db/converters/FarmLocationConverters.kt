package com.aumarbello.farmlog.data.db.converters

import androidx.room.TypeConverter
import com.aumarbello.farmlog.models.FarmLocation

class FarmLocationConverter {
    private val delimiter = ","

    @TypeConverter
    fun fromFarmLocation(location: FarmLocation): String {
        return "${location.latitude}$delimiter${location.longitude}"
    }

    @TypeConverter
    fun toFarmLocation(location: String): FarmLocation {
        val coordinates = location.split(delimiter)
        return FarmLocation(coordinates[0].toDouble(), coordinates[1].toDouble())
    }
}

class CoordinatesConverter {
    private val separator = "|"
    private val helper = FarmLocationConverter()

    @TypeConverter
    fun fromCoordinates(coordinates: List<FarmLocation>): String {
        return coordinates.joinToString(separator) { helper.fromFarmLocation(it) }
    }

    @TypeConverter
    fun toCoordinates(coordinates: String): List<FarmLocation> {
        val locations = coordinates.split(separator)

        return locations.map { helper.toFarmLocation(it) }
    }
}