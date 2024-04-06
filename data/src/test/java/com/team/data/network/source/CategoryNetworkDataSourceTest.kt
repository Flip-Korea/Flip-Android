package com.team.data.network.source

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.team.data.testdoubles.network.model.networkCategoriesTestData
import com.team.data.testdoubles.network.source.FakeCategoryNetworkDataSource
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class CategoryNetworkDataSourceTest {

    private lateinit var categoryNetworkApi: com.team.data.network.retrofit.api.CategoryNetworkApi
    private lateinit var categoryNetworkDataSource: com.team.data.network.source.CategoryNetworkDataSource
    private lateinit var server: MockWebServer
    private lateinit var moshi: Moshi

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()

        moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        categoryNetworkApi = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(server.url("/"))
            .build()
            .create(com.team.data.network.retrofit.api.CategoryNetworkApi::class.java)

        categoryNetworkDataSource = FakeCategoryNetworkDataSource(categoryNetworkApi)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    private suspend fun <T> getResult(response: Flow<Result<T, ErrorType>>): T? {
        return when (val result = response.first()) {
            is Result.Error -> { null }
            com.team.domain.util.Result.Loading -> { null }
            is Result.Success -> { result.data }
        }
    }

    @Test
    fun `getCategories Call Test`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(networkCategoriesTestData)
        })

        val actualResponse = getResult(categoryNetworkDataSource.getCategories())

        val adapter = moshi.adapter(com.team.data.network.model.response.ResponseCategoryWrapper::class.java)
        val expectedResponse = adapter.fromJson(networkCategoriesTestData)

        assertNotNull(actualResponse)
        assertEquals(expectedResponse!!.categories, actualResponse!!.categories)
    }
}