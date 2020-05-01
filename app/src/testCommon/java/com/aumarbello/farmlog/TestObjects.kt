package com.aumarbello.farmlog

import com.aumarbello.farmlog.models.FarmLocation
import com.aumarbello.farmlog.models.FarmLogEntity

object TestObjects {
    private val location = FarmLocation(25.774, -80.190)
    private val coordinates = listOf (
        FarmLocation(25.774, -80.190),
        FarmLocation(18.466, -66.118),
        FarmLocation(32.321, -64.757)
    )

    private const val imagePath = "home/aumarbello/Pictures/example.png"

    val entries = listOf(
        FarmLogEntity(imagePath, "Ola Musa", 35, "M", "Musa and sons Farm", location, coordinates, 1),
        FarmLogEntity(imagePath, "Chukwudi Deji", 29, "M", "CK Farms", location, coordinates, 2),
        FarmLogEntity(imagePath, "Sanni Banjo", 38, "M", "SB Farm Ltd", location, coordinates, 3),
        FarmLogEntity(imagePath, "Pauline John", 31, "F", "PJ Industrial Farm", location, coordinates, 4),
        FarmLogEntity(imagePath, "Seyi Kano", 37, "F", "SK Allied Farm", location, coordinates, 5)
    )

    val singleEntry = FarmLogEntity(imagePath, "Lizzy James", 43, "F", "L&J Farms", location, coordinates, 6)
}