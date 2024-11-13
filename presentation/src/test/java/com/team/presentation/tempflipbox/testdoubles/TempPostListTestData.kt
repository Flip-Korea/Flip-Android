package com.team.presentation.tempflipbox.testdoubles

import com.team.domain.model.post.TempPost
import com.team.domain.model.post.TempPostList
import com.team.domain.type.BackgroundColorType
import com.team.domain.type.FontStyleType

private val tempPostsTestData = List(15) {
    TempPost(it.toLong(), "테스트 Title #$it", "", BackgroundColorType.entries.random(), FontStyleType.NORMAL, 1, "일상", emptyList(), "2024-08-28 17:19:17")
}

internal val tempPostListTestData = TempPostList(tempPostsTestData, tempPostsTestData.size)