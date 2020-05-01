package com.aumarbello.farmlog.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "logs"
)
data class FarmLogEntity (
    val imagePath: String,
    val farmersName: String,
    val farmersAge: Int,
    val farmersGender: String,

    val farmName: String,
    val farmLocation: FarmLocation,
    val farmCoordinates: List<FarmLocation>,
    val dateCreated: Long,

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
)