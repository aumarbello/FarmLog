package com.aumarbello.farmlog.repositories

import com.aumarbello.farmlog.OpenForTesting
import com.aumarbello.farmlog.data.db.FarmLogDAO
import com.aumarbello.farmlog.models.FarmLogEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@OpenForTesting
@Singleton
class EntryRepository @Inject constructor(private val dao: FarmLogDAO) {
    suspend fun saveNewFarm(entry: FarmLogEntity) = withContext(Dispatchers.IO) {
        dao.insert(entry)
    }
}