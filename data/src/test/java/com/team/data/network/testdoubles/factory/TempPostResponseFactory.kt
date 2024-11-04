package com.team.data.network.testdoubles.factory

import com.team.data.network.model.response.post.TempPostResponse
import com.team.domain.type.BackgroundColorType
import com.team.domain.type.FontStyleType
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

class TempPostResponseFactory {

    private val counter = AtomicLong(0)

    fun create(): TempPostResponse {
        val id = counter.getAndIncrement()
        return TempPostResponse(
            tempPostId = id,
            title = "#$id title",
            content = "#$id content",
            bgColorType = BackgroundColorType.DEFAULT,
            fontStyleType = FontStyleType.NORMAL,
            categoryId = 0,
            categoryName = "",
            tags = emptyList(),
            postAt = "2024"
        )
    }
}