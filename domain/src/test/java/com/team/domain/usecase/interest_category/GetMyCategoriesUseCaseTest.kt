package com.team.domain.usecase.interest_category

import com.team.domain.repository.FakeUserRepository
import com.team.domain.usecase.category.myCategoriesTestData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class GetMyCategoriesUseCaseTest {

    @Test
    fun `Local DB 데이터 O`() = runTest {
        // Given
        val profileId = "profileId"
        val userRepository = FakeUserRepository(hasLocalData = true, isNetworkError = false)
        val getMyCategoriesUseCase = GetMyCategoriesUseCase(userRepository)

        // When
        val result = getMyCategoriesUseCase(profileId).first()

        // Then
        assertEquals(result.size, myCategoriesTestData.size)
    }

    @Test
    fun `Local DB 데이터 X, Network Fetch Successful`() = runTest {
        // Given
        val profileId = "profileId"
        val userRepository = FakeUserRepository(hasLocalData = false, isNetworkError = false)
        val getMyCategoriesUseCase = GetMyCategoriesUseCase(userRepository)

        // When
        val result = getMyCategoriesUseCase(profileId).first()

        // Then
        assertEquals(result.size, myCategoriesTestData.size)
    }

    @Test
    fun `Local DB 데이터 X, Network Fetch Failure`() = runTest {
        // Given
        val profileId = "profileId"
        val userRepository = FakeUserRepository(hasLocalData = false, isNetworkError = true)
        val getMyCategoriesUseCase = GetMyCategoriesUseCase(userRepository)

        // When
        val result = getMyCategoriesUseCase(profileId).first()

        // Then
        assert(result.isEmpty())
    }
}