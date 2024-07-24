package com.team.domain.usecase.profile

import com.team.domain.DataStoreManager
import com.team.domain.datastore.FakeDataStoreManager
import com.team.domain.repository.FakeUserRepository
import com.team.domain.type.DataStoreType
import com.team.domain.usecase.profile.testdoubles.myProfileTestData
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetMyProfileUseCaseTest {

    private lateinit var dataStoreManager: DataStoreManager

    @Before
    fun setUp() {
        dataStoreManager = FakeDataStoreManager()
    }

    @Test
    fun `Local DB 데이터 O`() = runTest {
        // Given
        val profileId = "profileId"
        val expected = myProfileTestData(profileId)

        dataStoreManager.saveData(DataStoreType.AccountType.CURRENT_PROFILE_ID, profileId)
        val userRepository = FakeUserRepository(
            profileId = profileId,
            hasLocalData = true,
            isNetworkError = false
        )
        val getMyProfileUseCase = GetMyProfileUseCase(dataStoreManager, userRepository)

        // When
        val actual = getMyProfileUseCase().last()

        // Then
        assertEquals(
            expected,
            (actual as Result.Success).data
        )
    }

    @Test
    fun `Local DB 데이터 X, Network Fetch Successful`() = runTest {
        // Given
        val profileId = "profileId"
        val expected = myProfileTestData(profileId)

        dataStoreManager.saveData(DataStoreType.AccountType.CURRENT_PROFILE_ID, profileId)
        val userRepository = FakeUserRepository(
            profileId = profileId,
            hasLocalData = false,
            isNetworkError = false
        )
        val getMyProfileUseCase = GetMyProfileUseCase(dataStoreManager, userRepository)

        // When
        val actual = getMyProfileUseCase().last()

        // Then
        assertEquals(
            expected,
            (actual as Result.Success).data
        )
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
        val getMyProfileUseCase = GetMyProfileUseCase(dataStoreManager, userRepository)

        // When
        val actual = getMyProfileUseCase().last()

        // Then
        assertNull((actual as Result.Success).data)
    }

    @Test
    fun `Profile ID를 찾을 수 없음`() = runTest {
        // Given
        val profileId = "profileId"
        val expected: Result<Nothing, ErrorType> = Result.Error(ErrorType.Auth.USER_NOT_FOUND)

        val userRepository = FakeUserRepository(
            profileId = profileId,
            hasLocalData = false,
            isNetworkError = false
        )
        val getMyProfileUseCase = GetMyProfileUseCase(dataStoreManager, userRepository)

        // When
        dataStoreManager.clearAll()
        val actual = getMyProfileUseCase().last()

        // Then
        assertEquals(expected, actual)
    }
}