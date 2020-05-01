package com.aumarbello.farmlog

import com.aumarbello.farmlog.models.DashboardItem.*
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
        FarmLogEntity(imagePath, "Ola Musa", 35, "M", "Musa and sons Farm", location, coordinates, 1588004815550,1),
        FarmLogEntity(imagePath, "Chukwudi Deji", 29, "M", "CK Farms", location, coordinates, 1588004815550,2),
        FarmLogEntity(imagePath, "Sanni Banjo", 38, "M", "SB Farm Ltd", location, coordinates, 1588091215550,3),
        FarmLogEntity(imagePath, "Pauline John", 31, "F", "PJ Industrial Farm", location, coordinates, 1588177615550, 4),
        FarmLogEntity(imagePath, "Seyi Kano", 37, "F", "SK Allied Farm", location, coordinates, 1588264015550,5)
    )

    val singleEntry = FarmLogEntity(imagePath, "Lizzy James", 43, "F", "L&J Farms", location, coordinates, 6)

    val dashboardItems = listOf(
        CountItem("Users onboarded", 5),
        BarChartItem(
            "Week's progress",
            mapOf("Sun" to 0, "Mon" to 2, "Tue" to 1, "Wed" to 1, "Thur" to 1, "Fri" to 0, "Sat" to 0)
        ),
        PieChartItem(
            "Age distribution",
            5,
            mapOf("29-32" to 2, "33-36" to 1, "37-38" to 2)
        ),
        PieChartItem("Gender distribution", 5, mapOf("M" to 3, "F" to 2))
    )
}