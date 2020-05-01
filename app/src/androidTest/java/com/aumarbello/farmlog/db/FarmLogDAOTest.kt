package com.aumarbello.farmlog.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aumarbello.farmlog.TestObjects
import com.aumarbello.farmlog.data.db.FarmLogDAO
import com.aumarbello.farmlog.data.db.FarmLogDatabase
import com.aumarbello.farmlog.getOrAwaitValue
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FarmLogDAOTest {
    private lateinit var farmLogDAO: FarmLogDAO
    private lateinit var database: FarmLogDatabase

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), FarmLogDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        farmLogDAO = database.logsDAO()
    }

    @Test
    fun writeLogsAndReadList() {
        farmLogDAO.insert(*TestObjects.entries.toTypedArray())

        val farmLogs = farmLogDAO.fetchLogs().getOrAwaitValue()

        assertThat(farmLogs, `is`(TestObjects.entries))
    }

    @Test
    fun writeLogsAndReadUpdatedList() {
        farmLogDAO.insert(*TestObjects.entries.toTypedArray())

        farmLogDAO.insert(TestObjects.singleEntry)

        val farmLogs = farmLogDAO.fetchLogs().getOrAwaitValue()
        val updatedFarmLogs = TestObjects.entries.toMutableList().apply {
            add(TestObjects.singleEntry)
        }.toList()

        assertThat(farmLogs, `is`(updatedFarmLogs))
    }

    @Test
    fun deleteLogAndReadUpdatedList() {
        val item = TestObjects.entries.first()
        farmLogDAO.insert(*TestObjects.entries.toTypedArray())

        farmLogDAO.delete(item)

        val farmLogs = farmLogDAO.fetchLogs().getOrAwaitValue()
        val updatedLogs = TestObjects.entries.subList(1, TestObjects.entries.size)

        assertThat(farmLogs, `is`(updatedLogs))
    }

    @After
    fun tearDown() {
        database.close()
    }
}