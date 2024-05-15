package com.vectorincng.squareandroidtaskvictor.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.vectorincng.squareandroidtaskvictor.MainCoroutineRule
import com.vectorincng.squareandroidtaskvictor.data.EmployeesRepository
import com.vectorincng.squareandroidtaskvictor.network.EmployeeFetcher
import com.vectorincng.squareandroidtaskvictor.runBlockingTest
import com.vectorincng.squareandroidtaskvictor.utilities.testEmployees
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import kotlin.jvm.Throws

@HiltAndroidTest
class HomeViewModelTest {
    private lateinit var viewModel: HomeViewModel

    private var repository: EmployeesRepository = mockk()
    private val hiltRule = HiltAndroidRule(this)
    private val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val coroutineRule = MainCoroutineRule()
    private val mockResponse = EmployeeFetcher.EmployeeDataResponse.Success(testEmployees)

    @get:Rule
    val rule: RuleChain = RuleChain
        .outerRule(hiltRule)
        .around(instantTaskExecutorRule)
        .around(coroutineRule)

    @Before
    fun setUp() {
        hiltRule.inject()
        viewModel = HomeViewModel(repository)
        coEvery { repository.updateEmployeeList(any()) } returns  flowOf(mockResponse)
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    @Test
    @Throws(InterruptedException::class)
    fun testDefaultValues() = coroutineRule.runBlockingTest  {
        val expected = HomeScreenUiState.Ready(testEmployees)
        Assert.assertTrue(viewModel.state.value == expected)
    }
}