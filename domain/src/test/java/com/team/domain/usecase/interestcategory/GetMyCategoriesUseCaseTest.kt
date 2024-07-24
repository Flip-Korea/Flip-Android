package com.team.domain.usecase.interestcategory

import com.team.domain.DataStoreManager
import com.team.domain.datastore.FakeDataStoreManager
import com.team.domain.repository.FakeUserRepository
import com.team.domain.type.DataStoreType
import com.team.domain.usecase.category.myCategoriesTestData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetMyCategoriesUseCaseTest {

    private lateinit var dataStoreManager: DataStoreManager

    @Before
    fun setUp() {
        dataStoreManager = FakeDataStoreManager()
    }

    @Test
    fun `Local DB 데이터 O`() = runTest {
        // Given
        val profileId = "profileId"
        val expected = myCategoriesTestData

        dataStoreManager.saveData(DataStoreType.AccountType.CURRENT_PROFILE_ID, profileId)
        val userRepository = FakeUserRepository(
            profileId = profileId,
            hasLocalData = true,
            isNetworkError = false
        )
        val getMyCategoriesUseCase = GetMyCategoriesUseCase(dataStoreManager, userRepository)

        // When
        val actual = getMyCategoriesUseCase().last()

        // Then
        assertNotNull(actual)
        assertEquals(expected.map { it.id }, actual)
    }

    @Test
    fun `Local DB 데이터 X, Network Fetch Successful`() = runTest {
        // Given
        val profileId = "profileId"
        val expected = myCategoriesTestData

        dataStoreManager.saveData(DataStoreType.AccountType.CURRENT_PROFILE_ID, profileId)
        val userRepository = FakeUserRepository(
            profileId = profileId,
            hasLocalData = false,
            isNetworkError = false
        )
        val getMyCategoriesUseCase = GetMyCategoriesUseCase(dataStoreManager, userRepository)

        // When
        val actual = getMyCategoriesUseCase().last()

        // Then
        assertNotNull(actual)
        assertEquals(expected.map { it.id }, actual)
    }

    @Test
    fun `Local DB 데이터 X, Network Fetch Failure`() = runTest {
        // Given
        val profileId = "profileId"

        dataStoreManager.saveData(DataStoreType.AccountType.CURRENT_PROFILE_ID, profileId)
        val userRepository = FakeUserRepository(
            profileId = profileId,
            hasLocalData = false,
            isNetworkError = true
        )
        val getMyCategoriesUseCase = GetMyCategoriesUseCase(dataStoreManager, userRepository)

        // When
        val actual = getMyCategoriesUseCase().last()

        // Then
        assertNull(actual)
    }

    @Test
    fun `Profile ID를 찾을 수 없음`() = runTest {
        // Given
        val profileId = "profileId"

        val userRepository = FakeUserRepository(
            profileId = profileId,
            hasLocalData = false,
            isNetworkError = true
        )
        val getMyCategoriesUseCase = GetMyCategoriesUseCase(dataStoreManager, userRepository)

        // When
        dataStoreManager.clearAll()
        val actual = getMyCategoriesUseCase().last()

        // Then
        assertNull(actual)
    }
}