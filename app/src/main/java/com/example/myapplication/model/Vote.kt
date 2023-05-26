package com.example.myapplication.model

import com.squareup.moshi.Json

/**
 * POST body data model for a vote
 */
data class Vote (

    @Json(name = "id")
    var id: String = "",

    @Json(name = "score")
    var score: Int = 0,

    @Json(name = "name")
    var name: String = "",

    @Json(name = "comment")
    var comment: String? = ""

)