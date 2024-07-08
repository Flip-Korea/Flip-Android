package com.team.data.repository

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.team.data.local.FlipDatabase
import com.team.data.local.dao.CategoryDao
import com.team.data.network.model.response.category.CategoryResponse
import com.team.data.network.model.response.category.toDomainModel
import com.team.data.network.retrofit.api.CategoryNetworkApi
import com.team.data.network.source.CategoryNetworkDataSource
import com.team.data.network.source.fake.FakeCategoryNetworkDataSource
import com.team.data.repository.fake.FakeCategoryRepository
import com.team.data.testdoubles.network.categoryEntitiesTestData
import com.team.data.testdoubles.network.networkCategoriesTestData
import com.team.domain.repository.CategoryRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject
import javax.inject.Named


@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = Config.NONE,
    sdk = [Build.VERSION_CODES.R],
    application = HiltTestApplication::class,
)
class DefaultCategoryRepositoryTest {

    @get:Rule(order = 1)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var categoryNetworkDataSource: CategoryNetworkDataSource
    private lateinit var categoryRepository: CategoryRepository

    private lateinit var categoryNetworkApi: CategoryNetworkApi
    private lateinit var server: MockWebServer
    private lateinit var moshi: Moshi

    @Inject
    @Named("test_db")
    lateinit var database: FlipDatabase
    private lateinit var categoryDao: CategoryDao

    @Before
    fun setUp() {
        hiltRule.inject()

        server = MockWebServer()
        server.start()

        moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        categoryNetworkApi = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(server.url("/"))
            .build()
            .create(CategoryNetworkApi::class.java)

        categoryDao = database.categoryDao()

        categoryNetworkDataSource = FakeCategoryNetworkDataSource(categoryNetworkApi)
        categoryRepository = FakeCategoryRepository(categoryDao, categoryNetworkDataSource)
    }

    @After
    fun teardown() {
        server.shutdown()
        database.close()
    }

    @Test
    fun `Local DB 에서 모든 카테고리 가져오기 (getCategoriesFromLocal())`() = runTest {
        assert(categoryRepository.getCategoriesFromLocal().first().isEmpty())

        categoryDao.upsertCategories(categoryEntitiesTestData)

        assertEquals(
            categoryRepository.getCategoriesFromLocal().first().size,
            categoryEntitiesTestData.size
        )
    }

    @Test
    fun `Network API 에서 모든 카테고리 가져오기 (getCategoriesFromNetwork())`() = runTest {
        categoryDao.clearAll()

        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(networkCategoriesTestData)
        })

        val listType = Types.newParameterizedType(List::class.java, CategoryResponse::class.java)
        val adapter: JsonAdapter<List<CategoryResponse>> = moshi.adapter(listType)
        val categories = adapter
                .fromJson(networkCategoriesTestData)
                ?.map { it.toDomainModel() }

        categoryRepository.refreshCategories()

        val categoryEntities = categoryDao.getCategories().first()

        assertNotNull(categories)
        assert(categoryEntities.isNotEmpty())
        assertEquals(categoryEntities.size, categories!!.size)
    }
}