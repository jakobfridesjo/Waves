package com.ltu.m7019e.v23.themoviedb.network

import com.example.myapplication.model.Station
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class StationResponse {
    @Json(name = "page")
    var page: Int = 0;

    @Json(name = "results")
    var results: List<Station> = listOf()

    @Json(name = "total_pages")
    var total_pages: Int = 0

    @Json(name = "total_results")
    var total_results: Int = 0
}