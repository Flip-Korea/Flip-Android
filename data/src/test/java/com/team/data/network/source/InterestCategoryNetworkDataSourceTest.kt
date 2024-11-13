package com.team.data.network.source

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.team.data.network.model.request.CategoryRequest
import com.team.data.network.model.response.category.CategoryResponse
import com.team.data.network.retrofit.api.InterestCategoryNetworkApi
import com.team.data.network.source.fake.FakeInterestCategoryNetworkDataSource
import com.team.domain.util.Result
import junit.framework.Assert
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class InterestCategoryNetworkDataSourceTest {

    private lateinit var interestCategoryNetworkApi: InterestCategoryNetworkApi
    private lateinit var interestCategoryNetworkDataSource: InterestCategoryNetworkDataSource
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

        interestCategoryNetworkDataSource = FakeInterestCategoryNetworkDataSource(interestCategoryNetworkApi)
    }

    @Test
    fun `나의 관심 카테고리 가져오기(getMyCategories())`() = runTest {
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

        val actualResponse = interestCategoryNetworkDataSource.getMyCategories()

        assert(expectedResponse != null)
        assertEquals(expectedResponse, (actualResponse as Result.Success).data)
    }

    @Test
    fun `나의 관심 카테고리 업데이트(updateMyCategories())`() = runTest {

        server.enqueue(MockResponse().apply {
            setResponseCode(201)
        })

        val response =
            interestCategoryNetworkDataSource.updateMyCategories(
                categoryIds = CategoryRequest(listOf(1, 2, 3)),
            )

        val recordedRequest = server.takeRequest()

        val adapter = moshi.adapter(CategoryRequest::class.java)
        val realRequestBody = adapter.fromJson(recordedRequest.body.peek())
        val requestBody = """
            {
                "categoryIds": [1,2,3]
            }
        """.trimIndent()
        val expectedRequestBody = adapter.fromJson(requestBody)

        Assert.assertNotNull(response)
        org.junit.Assert.assertEquals(expectedRequestBody!!.categoryIds, realRequestBody!!.categoryIds)
    }
}