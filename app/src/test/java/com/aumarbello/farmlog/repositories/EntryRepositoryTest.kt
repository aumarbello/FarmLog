package com.aumarbello.farmlog.repositories

import com.aumarbello.farmlog.TestObjects
import com.aumarbello.farmlog.data.db.FarmLogDAO
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.Mockito.verify

@RunWith(JUnit4::class)
class EntryRepositoryTest {
    private lateinit var dao: FarmLogDAO
    private lateinit var repository: EntryRepository

    @Before
    fun setUp() {
        dao = Mockito.mock(FarmLogDAO::class.java)

        repository = EntryRepository(dao)
    }

    @Test
    fun whenLogEntityIsPassedThenSaveToDao() = runBlocking {
        val entity = TestObjects.singleEntry
        repository.saveNewFarm(entity)

        verify(dao).insert(entity)
    }
}