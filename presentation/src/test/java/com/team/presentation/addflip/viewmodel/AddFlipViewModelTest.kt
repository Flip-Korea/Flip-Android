package com.team.presentation.addflip.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.team.domain.usecase.category.GetCategoriesUseCase
import com.team.domain.usecase.profile.GetCurrentProfileIdUseCase
import com.team.presentation.TestDispatcherRule
import com.team.presentation.addflip.testdoubles.categoriesTestData
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AddFlipViewModelTest {

    @get:Rule
    val testDispatcher = TestDispatcherRule()

    /** 백그라운드 작업을 동기적으로 실행 */
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var addFlipViewModel: AddFlipViewModel
    private val getCategoriesUseCase: GetCategoriesUseCase = mockk()
    private val getCurrentProfileIdUseCase: GetCurrentProfileIdUseCase = mockk()

    @Before
    fun setUp() {
        every { getCategoriesUseCase.invoke() } returns flowOf(categoriesTestData)
    }

    @Test
    fun `카테고리 가져오기`() = runTest {
        addFlipViewModel = AddFlipViewModel(getCurrentProfileIdUseCase, getCategoriesUseCase)

        val categoriesState = addFlipViewModel.categoriesState.first()

        assertEquals(categoriesState.categories,  categoriesTestData)
    }
}