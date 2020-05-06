package com.aumarbello.farmlog.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "logs"
)
data class FarmLogEntity (
    val imagePath: String,
    val farmersName: String,
    val farmersAge: Int,
    val farmersPhoneNumber: String,
    val farmersGender: String,

    val farmName: String,
    val farmLocation: FarmLocation,
    val farmCoordinates: List<FarmLocation>,
    val dateCreated: Long = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis,

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
)