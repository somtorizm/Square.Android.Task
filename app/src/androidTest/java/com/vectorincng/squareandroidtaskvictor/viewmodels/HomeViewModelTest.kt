package com.vectorincng.squareandroidtaskvictor.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.vectorincng.squareandroidtaskvictor.MainCoroutineRule
import com.vectorincng.squareandroidtaskvictor.data.EmployeesRepository
import com.vectorincng.squareandroidtaskvictor.network.EmployeeFetcher
import com.vectorincng.squareandroidtaskvictor.runBlockingTest
import com.vectorincng.squareandroidtaskvictor.utilities.testEmployees
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase
import kotlinx.coroutines.flow.flow
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import kotlin.jvm.Throws

@HiltAndroidTest
class HomeViewModelTest {
    private lateinit var viewModel: HomeViewModel
    private val hiltRule = HiltAndroidRule(this)
    private val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val coroutineRule = MainCoroutineRule()

    @get:Rule
    val rule: RuleChain = RuleChain
        .outerRule(hiltRule)
        .around(instantTaskExecutorRule)
        .around(coroutineRule)

    private var employeesRepository: EmployeesRepository = mockk()

    @Before
    fun setUp() {
        hiltRule.inject()
        viewModel = HomeViewModel(employeesRepository)
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    @Test
    @Throws(InterruptedException::class)
    fun testDefaultValues() = coroutineRule.runBlockingTest {
        Assert.assertTrue(viewModel.state.value == HomeScreenUiState.Loading)

    }

    @Suppress("BlockingMethodInNonBlockingContext")
    @Throws(InterruptedException::class)
    @Test
    fun testFetchResultSuccessState() = coroutineRule.runBlockingTest {
        val result = flow { emit(EmployeeFetcher.EmployeeDataResponse.Success(testEmployees)) }
        coEvery { employeesRepository.updateEmployeeList(true) } returns result
        viewModel.refresh(true)

        viewModel.state.test {
            TestCase.assertEquals(HomeScreenUiState.Loading, awaitItem())
            TestCase.assertEquals(HomeScreenUiState.Ready(testEmployees), awaitItem())
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    @Throws(InterruptedException::class)
    @Test
    fun testFetchResultErrorState() = coroutineRule.runBlockingTest {
        val result = flow { emit(EmployeeFetcher.EmployeeDataResponse.Error(throwable = null)) }
        coEvery { employeesRepository.updateEmployeeList(true) } returns result
        viewModel.refresh(true)

        viewModel.state.test {
            TestCase.assertEquals(HomeScreenUiState.Loading, awaitItem())
            TestCase.assertEquals(HomeScreenUiState.Error(null), awaitItem())
        }
    }
}