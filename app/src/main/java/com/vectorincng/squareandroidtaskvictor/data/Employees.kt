package com.vectorincng.squareandroidtaskvictor.data

import com.google.gson.annotations.SerializedName

data class Employees(
    @SerializedName("songs")
    val id: String,

    @SerializedName("full_name")
    val name: String,

    @SerializedName("photo_url_small")
    val imageUrl: String = "",

    val biography: String,
    val team: String,

)