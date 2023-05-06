package com.example.myapplication.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "stations")
data class Station (

    @Json()
    @PrimaryKey
    var stationUUID: String = "",

    @Json
    @ColumnInfo(name = "name")
    var name: String = "",

    @Json
    @TypeConverters(TagsConverter::class)
    @ColumnInfo(name = "tags")
    var tags: String? = "",

    @Json
    @ColumnInfo(name = "countrycode")
    var countryCode: String? = "",

    @Json
    @ColumnInfo(name = "country")
    var country: String? = "",

    @Json
    @ColumnInfo(name = "url")
    var url: String = "",

    @Json
    @ColumnInfo(name = "favicon")
    var favicon: String? = "",

    @Json
    @ColumnInfo(name = "geo_lat")
    var geo_lat: Double? = null,

    @Json
    @ColumnInfo(name = "geo_long")
    var geo_long: Double? = null

) : Parcelable

class TagsConverter {
    private val moshi = Moshi.Builder().build()
    private val listType = Types.newParameterizedType(List::class.java, String::class.java)
    @TypeConverter
    fun fromJson(json: String?): List<String> {
        if (json == null) {
            return emptyList()
        }
        val adapter = moshi.adapter<List<String>>(listType)
        return adapter.fromJson(json) ?: emptyList()
    }
}