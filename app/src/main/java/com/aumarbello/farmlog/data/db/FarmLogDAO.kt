package com.aumarbello.farmlog.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.aumarbello.farmlog.models.FarmLogEntity

@Dao
interface FarmLogDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg entries: FarmLogEntity)

    @Query("SELECT * FROM logs")
    fun fetchLogs(): LiveData<List<FarmLogEntity>>

    @Delete
    fun delete(item: FarmLogEntity)
}