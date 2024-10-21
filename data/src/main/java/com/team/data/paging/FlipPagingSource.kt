package com.team.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.team.data.network.networkCall
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import com.team.domain.util.paging.FlipPagingData
import com.team.domain.util.paging.FlipPagingTokens

/**
 * Flip 에서 사용하는 PagingSource
 *
 * - [T]: 응답으로 받으려는 데이터모델 (각 항목이 될 모델)
 * - [R]: T를 의존하고 있는 [FlipPagingData]를 구현하고 있는 데이터모델 (리스트를 포함하고 있음)
 *
 * [FlipPagingSource] 를 사용할 때 [R] <[FlipPagingData]> 타입의 데이터를 할당 해야 한다.
 *
 * ##### Remote API Sample
 * **Response<여기 데이터에 집중>**
 *  ```
 *  interface FlipTestApi {
 *     @GET("temp-posts")
 *     suspend fun getMyTempPost(
 *         @Query("limit") limit: Int,
 *         @Query("cursor") cursor: Long?,
 *     ): Response<TempPostListResponse>
 *  }
 *  ```
 *
 *  ##### TempPostListResponse
 *  ```
 *  @JsonClass(generateAdapter = true)
 *  data class TempPostListResponse(
 *      @Json(name = "tempPosts")
 *      override val pagingList: List<TempPostResponse>,
 *      val totalCount: Int,
 *  ) : FlipPagingData<TempPostResponse>
 *  ```
 *  응답으로 받으려는 데이터클래스에서 [FlipPagingData] 를 구현 해줘야 한다.
 *
 * @param apiCall 페이지네이션 api 호출
 * @param pageSize [FlipPagingTokens] 에서 PageSize 선택
 * @see FlipPagingData
 */
class FlipPagingSource<T : Any, R : FlipPagingData<T>>(
    private val apiCall: suspend (loadKey: Long?) -> Result<R, ErrorType>,
    private val pageSize: Int,
) : PagingSource<Long, T>() {

    override fun getRefreshKey(state: PagingState<Long, T>): Long? =
        state.anchorPosition?.let { anchorPosition ->
//            val anchorPage = state.closestPageToPosition(anchorPosition)
//            val refreshKey = anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
//            Log.d(tag, refreshKey.toString())
//            refreshKey
            null
        }

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, T> {
        try {
            val loadKey = params.key
            val result = apiCall(loadKey)
            if (result is Result.Error) {
                return LoadResult.Error(
                    FlipPagingException(
                        errorType = result.error,
                        errorBody = result.errorBody
                    ),
                )
            }

            /**
             * **`result as Result.Success`**
             *
             * [result]가 [Result.Error]일 경우는 위에서 얼리리턴 될 것 이고,
             * [apiCall]에서는 [networkCall]호출을 하기 때문에 [Result.Loading]을 반환하는 경우는 없다.
             * 그러므로, [Result.Error]가 아닌 경우는 무조건 [Result.Success]를 반환하는 경우이다.
             */
            val pageData = (result as Result.Success).data
//            val prevKey = getPreviousKey(loadKey)
//            val nextKey = getNextKey(loadKey, pageData)

            return LoadResult.Page(
                data = pageData.list,
                prevKey = pageData.firstKey,
                nextKey = pageData.lastKey,
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    private fun getPreviousKey(loadKey: Long?): Long? =
        loadKey?.let {
            val previousKey = (it - pageSize).coerceAtLeast(0)
            if (previousKey == 0L) null else previousKey
        }

    private fun getNextKey(
        loadKey: Long?,
        pagingData: List<T>,
    ): Long? {
        val nextKey: Long = loadKey?.let {
            it + pageSize
        } ?: pageSize.toLong()
        return if (pagingData.isEmpty()) null else nextKey
    }
}