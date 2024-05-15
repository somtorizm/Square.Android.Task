package com.vectorincng.squareandroidtaskvictor.data

import com.google.gson.annotations.SerializedName
import com.vectorincng.squareandroidtaskvictor.utils.Extensions

data class EmployeesResponse(
    val employees: List<EmployeesModel>
)


data class EmployeesModel(
    @field:SerializedName("uuid") val id: String,

    @field:SerializedName("full_name") val name: String,

    @field:SerializedName("photo_url_small") val imageUrl: String = "",

    @field:SerializedName("employee_type") val employeeType: Extensions.EmployeeType,

    val biography: String,
    val team: String,
)

