package com.aumarbello.farmlog.repositories

import com.aumarbello.farmlog.OpenForTesting
import com.aumarbello.farmlog.data.UserAuthenticator
import javax.inject.Inject

@OpenForTesting
class LoginRepository @Inject constructor(private val authenticator: UserAuthenticator) {
    suspend fun login(email: String, password: String): Boolean {
        return if (authenticator.authenticate(email, password)) {
            true
        } else
            throw IllegalArgumentException("Invalid email/password provides")
    }
}