package com.team.presentation.home.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.team.domain.DataStoreManager
import com.team.domain.model.category.Category
import com.team.domain.type.DataStoreType
import com.team.domain.usecase.category.GetCategoriesUseCase
import com.team.domain.usecase.interestcategory.GetMyCategoriesUseCase
import com.team.domain.usecase.post.GetPostsUseCase
import com.team.domain.usecase.post.PostUseCases
import com.team.domain.usecase.post.testdoubles.getPostListTestData
import com.team.domain.usecase.profile.GetCurrentProfileIdUseCase
import com.team.domain.util.ErrorBody
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import com.team.presentation.TestDispatcherRule
import com.team.presentation.fake.FakeDataStoreManager
import com.team.presentation.home.state.PostState
import com.team.presentation.home.testdoubles.categoriesTestData
import com.team.presentation.home.testdoubles.myCategoriesTestData
import com.team.presentation.util.UiText
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.time.Duration.Companion.seconds

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    val testDispatcher = TestDispatcherRule()

    /** 백그라운드 작업을 동기적으로 실행 */
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var homeViewModel: HomeViewModel
    private val postUseCases: PostUseCases = mockk()
    private val getPostsUseCase: GetPostsUseCase = mockk()
    private val getCategoriesUseCase: GetCategoriesUseCase = mockk()
    private val getMyCategoriesUseCase: GetMyCategoriesUseCase = mockk()
    private val getCurrentProfileIdUseCase: GetCurrentProfileIdUseCase = mockk()
    private lateinit var dataStoreManager: DataStoreManager

    @Before
    fun setUp() {
        dataStoreManager = FakeDataStoreManager()

        every { postUseCases.getPostsUseCase } returns getPostsUseCase
        every { getCategoriesUseCase.invoke() } returns flowOf(categoriesTestData)
        coEvery { getMyCategoriesUseCase() } returns flowOf(myCategoriesTestData)
        every { getCurrentProfileIdUseCase() } returns flowOf("currentProfileId")
    }


    @Test
    fun `카테고리 가져와서 정렬하기(고정 + 관심 카테고리 + 나머지)`() = runTest {
        // Given
        val profileId = "profileId"
        dataStoreManager.saveData(DataStoreType.AccountType.CURRENT_PROFILE_ID, profileId)

        val expected = getAlignedCategories()

        homeViewModel = HomeViewModel(
            UnconfinedTestDispatcher(),
            postUseCases,
            getMyCategoriesUseCase,
            getCategoriesUseCase,
            getCurrentProfileIdUseCase
        )

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
    }

    @Test
    fun `홈 화면 Post(FlipCard) 가져오기 - 성공 시`() = runTest {
        // Given
        val expectedPostList = getPostListTestData(15)
        every { getPostsUseCase(null) } returns flowOf(Result.Success(expectedPostList))

        homeViewModel = HomeViewModel(
            UnconfinedTestDispatcher(),
            postUseCases,
            getMyCategoriesUseCase,
            getCategoriesUseCase,
            getCurrentProfileIdUseCase
        )

        var postState: PostState? = null
        val job  = launch {
            homeViewModel.postState.collectLatest {
                postState = it
            }
        }

        // When (100 -> '전체' 카테고리)
        homeViewModel.getPostsByCategory(100)

        advanceTimeBy(1.seconds)
        job.cancel()

        // Then
        val actualPostList = postState?.posts

        assertEquals(expectedPostList.posts, actualPostList)
    }

    @Test
    fun `홈 화면 Post(FlipCard) 가져오기 - 실패 시`() = runTest {
        // Given
        val expectedErrorBody = ErrorBody(code = "", errors = null, message = "error")
        every { getPostsUseCase(null) } returns flowOf(Result.Error(error = ErrorType.Network.NOT_FOUND, errorBody = expectedErrorBody))

        homeViewModel = HomeViewModel(
            UnconfinedTestDispatcher(),
            postUseCases,
            getMyCategoriesUseCase,
            getCategoriesUseCase,
            getCurrentProfileIdUseCase
        )

        var postState: PostState? = null
        val job  = launch {
            homeViewModel.postState.collectLatest {
                postState = it
            }
        }

        // When (100 -> '전체' 카테고리)
        homeViewModel.getPostsByCategory(100)

        advanceTimeBy(1.seconds)
        job.cancel()

        // Then
        assert(postState?.loading == false)
        assert(postState!!.posts.isEmpty())
        assertEquals(UiText.DynamicString(expectedErrorBody.message), postState?.error)
    }

    /** getAlignedCategories(): HomeViewModel 에서 그대로 복사한 코드 */
    private suspend fun getAlignedCategories(): List<Category> {
        val categories = getCategoriesUseCase().first()
        val myCategoryIds = getMyCategoriesUseCase().first()
        return myCategoryIds?.let { myCateIds ->
            fixedCategories + myCateIds.mapNotNull { id ->
                categories.find { it.id == id }
            }
//            fixedCategories + categories.filter { myCateIds.contains(it.id) }
        } ?: fixedCategories
    }
}