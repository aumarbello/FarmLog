package com.aumarbello.farmlog.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.aumarbello.farmlog.OpenForTesting
import com.aumarbello.farmlog.data.db.FarmLogDAO
import com.aumarbello.farmlog.models.DashboardItem
import com.aumarbello.farmlog.models.DashboardItem.*
import com.aumarbello.farmlog.models.FarmLogEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.min

@OpenForTesting
@Singleton
class DashboardRepository @Inject constructor(private val dao: FarmLogDAO) {
    suspend fun createDashboardItems(): LiveData<List<DashboardItem>> =
        withContext(Dispatchers.IO) {
            dao.fetchLogs().map { getDashBoardItems(it) }
        }

    private fun getDashBoardItems(entities: List<FarmLogEntity>): List<DashboardItem> {
        if (entities.isEmpty())
            return emptyList()

        val items = mutableListOf<DashboardItem>()
        items.add(CountItem("Users onboarded", entities.size))
        items.add(resolveDaysOfWeek(entities))
        items.add(resolveAgeDistribution(entities))
        items.add(resolveGenderDistribution(entities))



        return items.toList()
    }

    private fun resolveDaysOfWeek(entities: List<FarmLogEntity>): BarChartItem {
        val daysMap = mutableMapOf(
            "Sun" to 0,
            "Mon" to 0,
            "Tue" to 0,
            "Wed" to 0,
            "Thur" to 0,
            "Fri" to 0,
            "Sat" to 0
        )


        val daysKeys = daysMap.keys.toList()
        entities.forEach {
            val dateCreated = Calendar.getInstance().apply {
                timeInMillis = it.dateCreated
            }
            val dayOfWeek = dateCreated[Calendar.DAY_OF_WEEK]
            val key = daysKeys[dayOfWeek.dec()]
            val currentValue = daysMap[key] ?: return@forEach

            daysMap[key] = currentValue.inc()
        }

        return BarChartItem("Week's progress", daysMap.toMap())
    }

    private fun resolveAgeDistribution(entities: List<FarmLogEntity>): PieChartItem {
        val title = "Age distribution"
        return when {
            entities.size == 1 -> PieChartItem(
                title,
                1,
                mapOf("${entities.first().farmersAge}" to 1)
            )

            entities.size < 4 -> {
                val ageList = entities.map { it.farmersAge }.sorted()
                PieChartItem(
                    title,
                    1,
                    mapOf("${ageList.first()}-${ageList.last()}" to 1)
                )
            }

            else -> {
                val ageMap = mutableMapOf<String, Int>()
                val ageList = entities.map { it.farmersAge }.sorted()

                val youngest = ageList.first()
                val oldest = ageList.last()
                val ageDifference = oldest - youngest

                var groups = if (ageDifference < 9)
                    2
                else
                    3
                val interval = ageDifference / groups

                var lowerBound = youngest
                var upperBound = min(lowerBound + interval, oldest)

                while (groups > 0) {
                    ageMap["$lowerBound-$upperBound"] = 0

                    lowerBound = upperBound.inc()
                    upperBound = min(lowerBound + interval, oldest)

                    groups--
                }

                val ageRanges = ageMap.keys.toList().map {
                    val range = it.split("-")
                    IntRange(range.first().toInt(), range.last().toInt())
                }

                entities.forEach {
                    val range =
                        ageRanges.find { age -> age.contains(it.farmersAge) } ?: return@forEach
                    val key = "${range.first}-${range.last}"
                    val currentValue = ageMap[key] ?: return@forEach

                    ageMap[key] = currentValue.inc()
                }

                PieChartItem(title, entities.size, ageMap.filter { it.value != 0 })
            }
        }
    }

    private fun resolveGenderDistribution(entities: List<FarmLogEntity>): PieChartItem {
        fun foldGender(gender: String): Pair<String, Int> {
            val value = entities.fold(0) { currentValue, entity ->
                if (entity.farmersGender == gender)
                    currentValue.inc()
                else
                    currentValue
            }

            return gender to value
        }

        return PieChartItem(
            "Gender distribution",
            entities.size,
            mapOf(foldGender("M"), foldGender("F"))
        )
    }
}