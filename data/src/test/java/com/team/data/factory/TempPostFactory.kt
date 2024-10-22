package com.team.data.factory

import com.team.domain.model.post.TempPost
import com.team.domain.type.BackgroundColorType
import java.util.concurrent.atomic.AtomicLong

class TempPostFactory {

    private val counter = AtomicLong(0)

    fun create(): TempPost {
        val id = counter.getAndIncrement()
        return TempPost(
            tempPostId = id,
            title = "#$id title",
            content = "#$id content",
            bgColorType = BackgroundColorType.DEFAULT,
            categoryId = 0,
            categoryName = "",
            postAt = "2024"
        )
    }
}