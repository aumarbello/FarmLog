package com.aumarbello.farmlog.viewmodels

import androidx.lifecycle.*
import com.aumarbello.farmlog.OpenForTesting
import com.aumarbello.farmlog.models.DashboardItem
import com.aumarbello.farmlog.repositories.DashboardRepository
import com.aumarbello.farmlog.utils.Event
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@OpenForTesting
@Singleton
class DashboardViewModel @Inject constructor(private val repository: DashboardRepository) : ViewModel() {
    private val _loader = MutableLiveData<Boolean>()
    val loader: LiveData<Boolean> = _loader

    private val _error = MutableLiveData<Event<String>>()
    val error: LiveData<Event<String>> = _error

    private val _response = MediatorLiveData<List<DashboardItem>>()
    val response: LiveData<List<DashboardItem>> = _response

    fun loadDashboard() {
        loadData {
            _response.addSource(repository.createDashboardItems()) {
                _response.value = it
            }
        }
    }

    private fun loadData(block: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                _loader.value = true
                block()
            } catch (ex: Throwable) {
                ex.printStackTrace()

                _error.value = Event(resolveErrorMessage(ex) ?: "An error occurred")
            } finally {
                _loader.value = false
            }
        }
    }

    private fun resolveErrorMessage(throwable: Throwable): String? {
        return if (throwable.message.isNullOrEmpty()) {
            null
        } else {
            throwable.message
        }
    }

    fun logOut() {
        viewModelScope.launch {
            repository.logOut()
        }
    }
}