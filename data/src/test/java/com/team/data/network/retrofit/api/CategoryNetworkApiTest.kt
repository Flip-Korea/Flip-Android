package com.team.data.network.retrofit.api

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.team.data.network.model.response.category.CategoryResponse
import com.team.data.network.testdoubles.networkCategoriesTestData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class CategoryNetworkApiTest {

    private lateinit var categoryNetworkApi: com.team.data.network.retrofit.api.CategoryNetworkApi
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
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `getCategories Call Test`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(networkCategoriesTestData)
        })

        val actualResponse = categoryNetworkApi.getCategories()

        val listType = Types.newParameterizedType(List::class.java, CategoryResponse::class.java)
        val adapter: JsonAdapter<List<CategoryResponse>> = moshi.adapter(listType)
        val expectedResponse = adapter.fromJson(networkCategoriesTestData)

        assertNotNull(actualResponse.body())
        assertEquals(200, actualResponse.code())
        assertEquals(expectedResponse!!, actualResponse.body()!!)
    }
}