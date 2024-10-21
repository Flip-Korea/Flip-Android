package com.team.data.paging

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

class PagingSourceParser(private val moshi: Moshi) {

    fun toJson(list: List<FakePagingResponse>): String {
        val pagingListType = Types.newParameterizedType(List::class.java, FakePagingResponse::class.java)
        val adapter: JsonAdapter<List<FakePagingResponse>> = moshi.adapter(pagingListType)
        return adapter.toJson(list)
    }

    fun toJson(fakePagingListResponse: FakePagingListResponse): String {
        val adapter = moshi.adapter(FakePagingListResponse::class.java)
        return adapter.toJson(fakePagingListResponse)
    }
}