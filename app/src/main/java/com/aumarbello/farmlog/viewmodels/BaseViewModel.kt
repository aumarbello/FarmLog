package com.aumarbello.farmlog.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aumarbello.farmlog.OpenForTesting
import com.aumarbello.farmlog.utils.Event
import kotlinx.coroutines.launch
import timber.log.Timber

@OpenForTesting
abstract class BaseViewModel<T> : ViewModel() {
    private val _loader = MutableLiveData<Boolean>()
    val loader: LiveData<Boolean> = _loader

    private val _error = MutableLiveData<Event<String>>()
    val error: LiveData<Event<String>> = _error

    private val _response = MutableLiveData<T>()
    val response: LiveData<T> = _response

    protected fun loadData(message: String, block: suspend () -> T) {
        viewModelScope.launch {
            try {
                _loader.value = true
                _response.value = block()

            } catch (ex: Throwable) {
                ex.printStackTrace()

                _error.value = Event(resolveErrorMessage(ex) ?: message)
            } finally {
                Timber.tag("ViewModel").d("Finally")
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
}