package com.aumarbello.farmlog.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.aumarbello.farmlog.TestObjects
import com.aumarbello.farmlog.data.db.FarmLogDAO
import com.aumarbello.farmlog.getOrAwaitValue
import com.aumarbello.farmlog.models.DashboardItem.PieChartItem
import com.aumarbello.farmlog.models.FarmLogEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.Mockito.`when`

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class DashboardRepositoryTest {
    private lateinit var repository: DashboardRepository
    private lateinit var dao: FarmLogDAO

    @get:Rule
    val executorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        dao = Mockito.mock(FarmLogDAO::class.java)

        repository = DashboardRepository(dao)
    }

    @Test
    fun resolveFarmLogsToAppropriateDashboardItems() = runBlocking {
        val response = MutableLiveData<List<FarmLogEntity>>().apply {
            value = TestObjects.entries
        }
        `when`(dao.fetchLogs()).thenReturn(response)

        val items = repository.createDashboardItems().getOrAwaitValue()
        assertThat(items, `is`(TestObjects.dashboardItems))
    }

    @Test
    fun whenLogIsEmptyReturnEmptyDashboardList() = runBlocking {
        val response = MutableLiveData<List<FarmLogEntity>>().apply {
            value = emptyList()
        }
        `when`(dao.fetchLogs()).thenReturn(response)

        val items = repository.createDashboardItems().getOrAwaitValue()
        assertThat(items, `is`(emptyList()))
    }

    @Test
    fun whenLogContainsSingleItemReturnSimpleChart() = runBlocking {
        val response = MutableLiveData<List<FarmLogEntity>>().apply {
            value = TestObjects.entries.subList(0, 1)
        }
        `when`(dao.fetchLogs()).thenReturn(response)

        val items = repository.createDashboardItems().getOrAwaitValue()

        val ageItem = items[2]
        val simpleChart = PieChartItem("Age distribution", 1, mapOf("${TestObjects.entries.first().farmersAge}" to 1))
        assertEquals(ageItem, simpleChart)
    }

    @Test
    fun whenLogSizeIsLessThanFourReturnMapWithSingleItem() = runBlocking {
        val logEntries = TestObjects.entries.subList(0, 3)
        val response = MutableLiveData<List<FarmLogEntity>>().apply {
            value = logEntries
        }
        `when`(dao.fetchLogs()).thenReturn(response)

        val items = repository.createDashboardItems().getOrAwaitValue()

        val ageItem = items[2]

        val ageList = logEntries.map { it.farmersAge }.sorted()
        val simpleChart = PieChartItem(
            "Age distribution",
            1,
            mapOf("${ageList.first()}-${ageList.last()}" to 1)
        )
        assertEquals(ageItem, simpleChart)
    }

    @Test
    fun whenEmptyAgeGroupsExistRemoveFromDashboardItems() = runBlocking {
        val response = MutableLiveData<List<FarmLogEntity>>().apply {
            value = TestObjects.entries.subList(1, TestObjects.entries.size)
        }
        `when`(dao.fetchLogs()).thenReturn(response)

        val items = repository.createDashboardItems().getOrAwaitValue()

        val ageItem = items[2] as PieChartItem
        ageItem.groups.forEach { _, value ->
            assertThat(value, `is`(not(0)))
        }
    }
}