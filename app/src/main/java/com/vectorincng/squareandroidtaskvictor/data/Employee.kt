package com.vectorincng.squareandroidtaskvictor.data

import com.vectorincng.squareandroidtaskvictor.utils.Extensions

data class Employee (
    val id: String,
    val name: String,
    val imageUrl: String = "",
    val biography: String,
    val team: String,
    val employeeType: Extensions.EmployeeType
)