package com.team.data.network.retrofit.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.team.data.network.model.request.CategoryRequest
import com.team.data.network.model.response.category.CategoryResponse
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
class InterestCategoryNetworkApiTest {

    private lateinit var interestCategoryNetworkApi: InterestCategoryNetworkApi
    private lateinit var server: MockWebServer
    private lateinit var moshi: Moshi

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()

        moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        interestCategoryNetworkApi = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(server.url("/"))
            .build()
            .create(InterestCategoryNetworkApi::class.java)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `getMyCategories Call Test`() = runTest {
        val myCategories = """
                [ {
                  "categoryId" : 1,
                  "categoryName" : "일상"
                }, {
                  "categoryId" : 2,
                  "categoryName" : "IT과학"
                } ]
            """.trimIndent()
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(myCategories)
        })

        val adapter = Types.newParameterizedType(List::class.java, CategoryResponse::class.java)
        val expectedResponse = moshi.adapter<List<CategoryResponse>?>(adapter)
            .fromJson(myCategories)

        val actualResponse = interestCategoryNetworkApi.getMyCategories()

        assert(expectedResponse != null)
        assert(actualResponse.isSuccessful)
        assertEquals(expectedResponse, actualResponse.body())
    }

    @Test
    fun `updateMyCategories Call Test`() = runTest {

        server.enqueue(MockResponse().apply {
            setResponseCode(201)
        })

        val categoryRequest = CategoryRequest(listOf(1, 2, 3))

        val response =
            interestCategoryNetworkApi.updateMyCategories(
                categoryIds = categoryRequest
            )

        val recordedRequest = server.takeRequest()

        val adapter = moshi.adapter(CategoryRequest::class.java)
        val actualRequestBody = adapter.fromJson(recordedRequest.body.peek())

        assertEquals(201, response.code())
        assertNotNull(actualRequestBody)
        assertEquals(
            categoryRequest.categoryIds,
            actualRequestBody!!.categoryIds
        )
    }
}