package com.aumarbello.farmlog.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.aumarbello.farmlog.data.db.converters.CoordinatesConverter
import com.aumarbello.farmlog.data.db.converters.FarmLocationConverter
import com.aumarbello.farmlog.models.FarmLogEntity
import com.aumarbello.farmlog.utils.DatabaseConstants

@Database(entities = [FarmLogEntity::class], version = DatabaseConstants.Version, exportSchema = false)
@TypeConverters(FarmLocationConverter::class, CoordinatesConverter::class)
abstract class FarmLogDatabase:  RoomDatabase() {
    abstract fun logsDAO(): FarmLogDAO

    companion object {
        private lateinit var database: FarmLogDatabase

        fun getInstance(context: Context): FarmLogDatabase {
            if (!::database.isInitialized) {
                synchronized(this) {
                    database = Room.databaseBuilder(
                        context,
                        FarmLogDatabase::class.java,
                        "logs.db"
                    ).build()
                }
            }

            return database
        }
    }
}