package com.team.data.paging

import java.util.concurrent.atomic.AtomicInteger

class PagingResponseFactory {
    private val counter = AtomicInteger(0)

    private fun createPagingResponse(): FakePagingResponse {
        val id = counter.getAndIncrement()
        return FakePagingResponse(
            id = id.toLong(),
            content = "#$id content"
        )
    }

    fun createPagingListResponse(limit: Int): FakePagingListResponse {
        val list = List(limit) {
            createPagingResponse()
        }
        return FakePagingListResponse(
            list = list,
            listTotal = 100L
        )
    }
}