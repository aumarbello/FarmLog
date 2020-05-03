package com.aumarbello.farmlog.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.aumarbello.farmlog.CoroutineTestRule
import com.aumarbello.farmlog.TestObjects
import com.aumarbello.farmlog.mock
import com.aumarbello.farmlog.repositories.EntryRepository
import com.aumarbello.farmlog.utils.Event
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.Mockito.`when`

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class EntryViewModelTest {
    @get:Rule
    var rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = CoroutineTestRule()

    private lateinit var repository: EntryRepository
    private lateinit var viewModel: EntryViewModel

    @Before
    fun setUp() {
        repository = Mockito.mock(EntryRepository::class.java)

        viewModel = EntryViewModel(repository)
    }

    @Test
    fun whenRequestSucceedsThenUpdateResponse() = runBlockingTest {
        `when`(repository.saveNewFarm(TestObjects.singleEntry)).thenReturn(Unit)

        viewModel.addFarm(TestObjects.singleEntry)

        val observer = mock<Observer<Unit>>()
        viewModel.response.observeForever(observer)

        Mockito.verify(observer).onChanged(Unit)
    }

    @Test
    fun whenRequestFailsThenUpdateError() = runBlockingTest {
        val errorMsg =  "Failed to login, try again later."

        `when`(repository.saveNewFarm(TestObjects.singleEntry)).thenThrow(IllegalArgumentException(errorMsg))

        viewModel.addFarm(TestObjects.singleEntry)

        val observer = mock<Observer<Event<String>>>()
        viewModel.error.observeForever(observer)

        assertThat(viewModel.error.value?.peekContent(), `is`(errorMsg))
    }
}