package com.example.myapplication.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.myapplication.model.Station

@Dao
interface StationDatabaseDao {
    @Insert
    fun insert(station: Station)

    @Delete
    fun delete(station: Station)

    @Query("SELECT * FROM stations ORDER BY name")
    fun getAllStations(): List<Station>
}