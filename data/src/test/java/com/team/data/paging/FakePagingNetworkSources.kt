package com.team.data.paging

import com.squareup.moshi.JsonClass
import com.team.domain.util.paging.FlipPagingData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

@JsonClass(generateAdapter = true)
data class FakePagingListResponse(
    override val list: List<FakePagingResponse>,
    val listTotal: Long
) : FlipPagingData<FakePagingResponse> {
    override val firstKey: Long?
        get() = if (list.first().id == 0L) null else list.first().id
    override val lastKey: Long
        get() = list.last().id
}

data class FakePagingResponse(
    val id: Long,
    val content: String
)

interface FakePagingApiService {
    @GET("/pagination")
    suspend fun getPosts(
        @Query("cursor") cursor: String? = null,
        @Query("limit") limit: Int
    ): Response<FakePagingListResponse>
}