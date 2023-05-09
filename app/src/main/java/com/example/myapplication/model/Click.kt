package com.example.myapplication.model

import com.squareup.moshi.Json

data class Click (

    @Json(name = "id")
    var id: String = "",

    @Json(name = "name")
    var name: String = "",

    @Json(name = "click_url")
    var clickUrl: String = ""

)