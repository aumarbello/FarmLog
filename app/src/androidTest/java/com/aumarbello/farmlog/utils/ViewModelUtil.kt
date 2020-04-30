package com.aumarbello.farmlog.utils

import androidx.lifecycle.ViewModel
import com.aumarbello.farmlog.di.FarmLogViewModelFactory
import javax.inject.Provider

/**
 * Creates a one off view model factory for the given view model instance.
 */
object ViewModelUtil {
    fun <T : ViewModel> createFor(model: T): FarmLogViewModelFactory {
        return FarmLogViewModelFactory(
            mapOf(
                model::class.java to providerOf(
                    model
                )
            )
        )
    }

    private fun <T : ViewModel> providerOf(model: T): Provider<ViewModel> {
        return Provider { model }
    }
}