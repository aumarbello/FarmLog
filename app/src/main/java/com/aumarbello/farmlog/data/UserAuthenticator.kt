package com.aumarbello.farmlog.data

interface UserAuthenticator {
    suspend fun authenticate(email: String, password: String): Boolean
}