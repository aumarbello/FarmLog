package com.aumarbello.farmlog.data

import android.content.Context

/**
 * Simple in-memory authenticator that validates user credentials against
 * a list of valid credentials
 */
class InMemoryAuthenticator (context: Context): UserAuthenticator {
    private val preferences = context.getSharedPreferences("farm-log", Context.MODE_PRIVATE)

    private val validCredentials = listOf(
        "test@theagromall.com" to "password"
    )

    override suspend fun authenticate(email: String, password: String): Boolean {
        val isValid = validCredentials.contains(email to password)
        if (isValid) {
            preferences.edit().putBoolean(loggedIn, true).apply()
        }

        return isValid
    }


    override fun isUserLoggedIn(): Boolean {
        return preferences.getBoolean(loggedIn, false)
    }

    private companion object {
        const val loggedIn = "userLoggedIn"
    }
}