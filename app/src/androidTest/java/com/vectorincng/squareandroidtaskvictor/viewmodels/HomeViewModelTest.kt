package com.vectorincng.squareandroidtaskvictor.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.vectorincng.squareandroidtaskvictor.MainCoroutineRule
import com.vectorincng.squareandroidtaskvictor.data.EmployeesRepository
import com.vectorincng.squareandroidtaskvictor.runBlockingTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import javax.inject.Inject
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

    @Inject
    lateinit var employeesRepository: EmployeesRepository

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
}