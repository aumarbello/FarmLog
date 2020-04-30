package com.aumarbello.farmlog.viewmodels

import com.aumarbello.farmlog.OpenForTesting
import com.aumarbello.farmlog.repositories.LoginRepository
import javax.inject.Inject

@OpenForTesting
class LoginViewModel @Inject constructor(private val repository: LoginRepository): BaseViewModel<Boolean>() {
    fun login(email: String, password: String) {
        loadData("Failed to login") { repository.login(email, password) }
    }
}