package com.example.myapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.model.Station
import com.example.myapplication.model.StationAttributes

@Database(entities = [Station::class, StationAttributes::class], version = 1, exportSchema = false)
abstract class StationDatabase : RoomDatabase() {
    abstract val stationDatabaseDao: StationDatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: StationDatabase? = null

        /**
         * Get the database instance
         */
        fun getInstance(context: Context): StationDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        StationDatabase::class.java,
                        "saved_station_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}