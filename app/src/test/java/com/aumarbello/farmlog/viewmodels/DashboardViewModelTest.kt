package com.aumarbello.farmlog.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.aumarbello.farmlog.CoroutineTestRule
import com.aumarbello.farmlog.TestObjects
import com.aumarbello.farmlog.mock
import com.aumarbello.farmlog.models.DashboardItem
import com.aumarbello.farmlog.repositories.DashboardRepository
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
import org.mockito.Mockito.verify

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class DashboardViewModelTest {
    @get:Rule
    var rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = CoroutineTestRule()

    private lateinit var repository: DashboardRepository
    private lateinit var viewModel: DashboardViewModel

    @Before
    fun setUp() {
        repository = Mockito.mock(DashboardRepository::class.java)

        viewModel = DashboardViewModel(repository)
    }

    @Test
    fun initialLoadingState() = runBlockingTest {
        val observer = mock<Observer<Boolean>>()
        viewModel.loader.observeForever(observer)

        viewModel.loadDashboard()

        verify(observer).onChanged(true)
    }

    @Test
    fun whenRequestIsSuccessfulUpdateResponse() = runBlockingTest {
        val response = MutableLiveData<List<DashboardItem>>().apply {
            value = TestObjects.dashboardItems
        }
        `when`(repository.createDashboardItems()).thenReturn(response)


        viewModel.loadDashboard()

        val observer = mock<Observer<List<DashboardItem>>>()
        viewModel.response.observeForever(observer)

        verify(observer).onChanged(TestObjects.dashboardItems)
    }

    @Test
    fun whenRequestFailsUpdateError() = runBlockingTest {
        val errorMsg =  "Failed to login, try again later."

        `when`(repository.createDashboardItems()).thenThrow(IllegalArgumentException(errorMsg))

        viewModel.loadDashboard()

        val observer = mock<Observer<Event<String>>>()
        viewModel.error.observeForever(observer)

        assertThat(viewModel.error.value?.peekContent(), `is`(errorMsg))
    }
}