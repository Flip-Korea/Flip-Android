package com.team.presentation.home.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.team.domain.DataStoreManager
import com.team.domain.model.category.Category
import com.team.domain.type.DataStoreType
import com.team.domain.usecase.category.GetCategoriesUseCase
import com.team.domain.usecase.interestcategory.GetMyCategoriesUseCase
import com.team.domain.usecase.post.PostUseCases
import com.team.presentation.TestDispatcherRule
import com.team.presentation.fake.FakeDataStoreManager
import com.team.presentation.home.viewmodel.testdoubles.categoriesTestData
import com.team.presentation.home.viewmodel.testdoubles.myCategoriesTestData
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    val testDispatcher = TestDispatcherRule()

    /** 백그라운드 작업을 동기적으로 실행 */
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var homeViewModel: HomeViewModel
    private val postUseCases: PostUseCases = mockk()
    private val getCategoriesUseCase: GetCategoriesUseCase = mockk()
    private val getMyCategoriesUseCase: GetMyCategoriesUseCase = mockk()
    private lateinit var dataStoreManager: DataStoreManager

    @Before
    fun setUp() {
        dataStoreManager = FakeDataStoreManager()
    }


    @Test
    fun `카테고리 가져와서 정렬하기(고정 + 관심 카테고리 + 나머지)`() = runTest {
        // Given
        val profileId = "profileId"
        dataStoreManager.saveData(DataStoreType.AccountType.CURRENT_PROFILE_ID, profileId)
        every { getCategoriesUseCase.invoke() } returns flowOf(categoriesTestData)
        every { getMyCategoriesUseCase.invoke(profileId) } returns flowOf(myCategoriesTestData)

        val expected = getAlignedCategories()

        homeViewModel = HomeViewModel(postUseCases, getCategoriesUseCase, getMyCategoriesUseCase, dataStoreManager)

        /** 코루틴(비동기 작업) 다 기다림 */
        advanceUntilIdle()

        // When
        val categoriesState = homeViewModel.categoriesState.first()

        // Then
        val actual = categoriesState.categories
        assert(actual.isNotEmpty())
        assertEquals(expected, actual)
        assertEquals(expected[2], actual[2])
        assertEquals(expected[4], actual[4])
        assertEquals(expected[7], actual[7])
        assertEquals(expected[10], actual[10])
    }

    /** getAlignedCategories(): HomeViewModel 에서 그대로 복사한 코드 */
    private suspend fun getAlignedCategories(): List<Category> {
        val currentProfileId =
            dataStoreManager.getData(DataStoreType.AccountType.CURRENT_PROFILE_ID).first()
        val categories = getCategoriesUseCase().first()
        val myCategories = getMyCategoriesUseCase(currentProfileId ?: "").first()
        val allCategories = fixedCategories + categories.sortedByDescending { cate ->
            myCategories.contains(cate.id)
        }
        return allCategories
    }
}