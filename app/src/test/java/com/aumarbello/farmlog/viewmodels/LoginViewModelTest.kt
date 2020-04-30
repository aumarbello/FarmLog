package com.aumarbello.farmlog.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.aumarbello.farmlog.CoroutineTestRule
import com.aumarbello.farmlog.mock
import com.aumarbello.farmlog.repositories.LoginRepository
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
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class LoginViewModelTest {
    @get:Rule
    var rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = CoroutineTestRule()

    private lateinit var repository: LoginRepository
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        repository = Mockito.mock(LoginRepository::class.java)

        viewModel = LoginViewModel(repository)
    }

    @Test
    fun initialLoadingState() = runBlockingTest {
        val email = "test@example.com"
        val password = "password"

        val observer = mock<Observer<Boolean>>()
        viewModel.loader.observeForever(observer)

        viewModel.login(email, password)

        verify(observer).onChanged(true)
    }

    @Test
    fun whenLoginSuccessfulUpdateResponse() = runBlockingTest {
        val email = "test@example.com"
        val password = "password"

        `when`(repository.login(anyString(), anyString())).thenReturn(true)


        viewModel.login(email, password)

        val observer = mock<Observer<Boolean>>()
        viewModel.response.observeForever(observer)

        verify(observer).onChanged(true)
    }

    @Test
    fun whenLoginFailsOccursUpdateError() = runBlockingTest {
        val errorMsg =  "Failed to login, try again later."
        val email = "test@example.com"
        val password = "password"

        `when`(repository.login(anyString(), anyString())).thenThrow(IllegalArgumentException(errorMsg))

        viewModel.login(email, password)

        val observer = mock<Observer<Event<String>>>()
        viewModel.error.observeForever(observer)

        assertThat(viewModel.error.value?.peekContent(), `is`(errorMsg))
    }
}