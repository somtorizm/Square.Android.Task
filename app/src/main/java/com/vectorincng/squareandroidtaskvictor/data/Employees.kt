package com.vectorincng.squareandroidtaskvictor.data

import com.google.gson.annotations.SerializedName

data class Employees(
    @SerializedName("songs")
    val id: String,

    val name: String,
    val description: String,
    val growZoneNumber: Int,
    val wateringInterval: Int = 7, // how often the plant should be watered, in days
    val imageUrl: String = ""
)