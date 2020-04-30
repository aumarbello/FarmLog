package com.aumarbello.farmlog.repositories

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class LoginRepositoryTest {
    private lateinit var repository: LoginRepository

    @Before
    fun setUp() {
        repository = LoginRepository(FakeUserAuthenticator())
    }

    @Test
    fun whenCredentialsAreValidReturnTrue() = runBlockingTest {
        val email = "test@theagromall.com"
        val password = "password"

        val response = repository.login(email, password)

        assertThat(response, `is`(true))
    }

    @Test(expected = IllegalArgumentException::class)
    fun whenCredentialsAreInvalidThrowException() = runBlockingTest {
        val email = "test@theagromall.com"
        val password = "pass"

        repository.login(email, password)
    }
}