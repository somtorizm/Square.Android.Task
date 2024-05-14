package com.vectorincng.squareandroidtaskvictor.data

import com.google.gson.annotations.SerializedName

data class EmployeesResponse(
    @field:SerializedName("full_name") val name: String,

    @field:SerializedName("photo_url_small") val imageUrl: String = "",

    val biography: String,
    val team: String,
)