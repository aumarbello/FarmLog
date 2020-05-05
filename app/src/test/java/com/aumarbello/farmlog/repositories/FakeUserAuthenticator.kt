package com.aumarbello.farmlog.repositories

import com.aumarbello.farmlog.data.UserAuthenticator

class FakeUserAuthenticator (
    private val testEmail: String = "test@theagromall.com",
    private val testPassword: String = "password"
): UserAuthenticator {
    override suspend fun authenticate(email: String, password: String): Boolean {
        return email == testEmail && password == testPassword
    }

    override fun isUserLoggedIn() = false
}