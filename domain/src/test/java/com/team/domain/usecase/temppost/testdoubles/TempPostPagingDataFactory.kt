package com.team.domain.usecase.temppost.testdoubles

import androidx.paging.PagingData
import com.team.domain.model.post.TempPost

class TempPostPagingDataFactory {

    private val tempPostFactory = TempPostFactory()

    fun create(pageSize: Int): PagingData<TempPost> {
        val list = List(pageSize) {
            tempPostFactory.create()
        }
        return PagingData.from(list)
    }

    fun empty(): PagingData<TempPost> {
        return PagingData.empty()
    }
}