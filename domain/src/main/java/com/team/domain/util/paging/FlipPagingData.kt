package com.team.domain.util.paging

/**
 * 페이지네이션에 사용하는 데이터 모델에서 해당 인터페이스를 구현해야 한다.
 *
 * 페이지네이션을 적용할 리스트 데이터를 [list]에 getter로 지정해줘야 한다.
 *
 * ```
 * @JsonClass(generateAdapter = true)
 * data class TempPostListResponse(
 *     @Json(name = "tempPosts")
 *     override val pagingList: List<TempPostResponse>,
 *     val totalCount: Int,
 * ) : FlipPagingData<TempPostResponse>
 * ```
 */
interface FlipPagingData <T> {
    /** 페이징 리스트 */
    val list: List<T>
    val firstKey: Long?
    val lastKey: Long?
}