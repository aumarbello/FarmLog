package com.aumarbello.farmlog.viewmodels

import com.aumarbello.farmlog.OpenForTesting
import com.aumarbello.farmlog.models.FarmLogEntity
import com.aumarbello.farmlog.repositories.EntryRepository
import javax.inject.Inject

@OpenForTesting
class EntryViewModel @Inject constructor(private val repository: EntryRepository): BaseViewModel<Unit>() {
    fun addFarm(entry: FarmLogEntity) {
        loadData("Unable to save new farm") { repository.saveNewFarm(entry) }
    }

}