package com.team.domain.usecase.category

import com.team.domain.repository.CategoryRepository
import com.team.domain.repository.FakeCategoryRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

@ExperimentalCoroutinesApi
class GetCategoriesUseCaseTest {

//    private val categoryRepository: CategoryRepository = mockk()

    /**
     * 테스트 데이터는 categoriesTestData,
     * 이하 생략
     */
    @Test
    fun `Local DB 데이터 O`() = runTest {
        // Given
        val categoryRepository: CategoryRepository =
            FakeCategoryRepository(hasLocalData = true, isNetworkError = false)
        val getCategoriesUseCase = GetCategoriesUseCase(categoryRepository)

        // When
        val categories = getCategoriesUseCase().firstOrNull()

        // Then
        assertNotNull(categories)
        assertEquals(categories?.size, categoriesTestData.size)
    }

    @Test
    fun `Local DB 데이터 X, Network Fetch Successful`() = runTest {
        // 1.
        // Given
        val categoryRepository: CategoryRepository =
            FakeCategoryRepository(hasLocalData = false, isNetworkError = false)
        val getCategoriesUseCase = GetCategoriesUseCase(categoryRepository)

        // When
        val categories = getCategoriesUseCase().firstOrNull()

        // Then
        assertNotNull(categories)
        assertEquals(categories?.size, categoriesTestData.size)
    }

    @Test
    fun `Local DB 데이터 X, Network Fetch Failure`() = runTest {
        // Given
        val categoryRepository: CategoryRepository =
            FakeCategoryRepository(hasLocalData = false, isNetworkError = true)
        val getCategoriesUseCase = GetCategoriesUseCase(categoryRepository)

        // When
        val categories = getCategoriesUseCase().firstOrNull()

        // Then
        assertNotNull(categories)
        assert(categories!!.isEmpty())
    }
}