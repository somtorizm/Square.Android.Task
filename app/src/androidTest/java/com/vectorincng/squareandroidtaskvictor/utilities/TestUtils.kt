

package com.vectorincng.squareandroidtaskvictor.utilities


import com.vectorincng.squareandroidtaskvictor.network.EmployeeFetcher
import com.vectorincng.squareandroidtaskvictor.utils.Extensions

/**
 * [Employee] objects used for tests.
 */
val testEmployees = arrayListOf(
    EmployeeFetcher.EmployeeDataResponse.Employee("1", "Apple", "A red fruit", "","", Extensions.EmployeeType.CONTRACTOR),
)
val testEmployee = testEmployees[0]


