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

    @Json(name = "stationuuid")
    @PrimaryKey
    var stationUUID: String = "",

    @Json(name = "name")
    @ColumnInfo(name = "name")
    var name: String = "",

    @Json(name = "tags")
    @ColumnInfo(name = "tags")
    @TypeConverters(TagsConverter::class)
    var tags: String? = "",

    @Json(name = "countrycode")
    @ColumnInfo(name = "countrycode")
    var countryCode: String? = "",

    @Json(name = "country")
    @ColumnInfo(name = "country")
    var country: String? = "",

    @Json(name = "url")
    @ColumnInfo(name = "url")
    var url: String = "",

    @Json(name = "url_resolved")
    @ColumnInfo(name = "url_resolved")
    var urlResolved: String = "",

    @Json(name = "favicon")
    @ColumnInfo(name = "favicon")
    var favicon: String? = "",

    @Json(name = "geo_lat")
    @ColumnInfo(name = "geo_lat")
    var geo_lat: Double? = null,

    @Json(name = "geo_long")
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