package com.aumarbello.farmlog.data

/**
 * Simple in-memory authenticator that validates user credentials against
 * a list of valid credentials
 */
class InMemoryAuthenticator: UserAuthenticator {
    private val validCredentials = listOf(
        "test@theagromall.com" to "password"
    )

    override suspend fun authenticate(email: String, password: String): Boolean {
        return validCredentials.contains(email to password)
    }
}