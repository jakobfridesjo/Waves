package com.example.myapplication.model

import com.squareup.moshi.Json

data class Click (

    var id: String = "",

    @Json(name = "click")
    var click: Int = 1

)