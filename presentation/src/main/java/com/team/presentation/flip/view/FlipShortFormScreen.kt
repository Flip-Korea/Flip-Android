package com.team.presentation.flip.view

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.team.domain.model.post.Post
import com.team.domain.model.profile.DisplayProfile
import com.team.domain.type.BackgroundColorType
import com.team.domain.type.FlipContentSeparator
import com.team.domain.util.FlipPagination

/**
 * Flip 숏폼 형태
 *
 * @param posts 플립(Post) 리스트 (페이지네이션이 적용 되어 있다면, [posts]사이즈 == PageSize
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FlipShortFormScreen(
    modifier: Modifier = Modifier,
    posts: List<Post>
) {
    val pagerState = rememberPagerState { FlipPagination.PAGE_SIZE }

    VerticalPager(
        modifier = modifier.fillMaxSize(),
        state = pagerState
    ) { page ->

        key(posts[page].postId) {
            FlipScreen(
                post = posts[page]
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FlipShortFormScreenPreview() {
    FlipShortFormScreen(posts = PostsTestData)
}

private val PostsTestData = List(15) {
    Post(
        postId = it.toLong(),
        profile = DisplayProfile(profileId = "profileId", nickname = "홍길동", photoUrl = ""),
        title = "행정권은 대통령을 수반으로 하는 정부에 속한다.",
        content = "가부동수인 때에는 부결된 것으로 본다.\n" +
                "행정권은 대통령을 수반으로 하는 정부에\n" +
                "속한다. 모든 국민은 헌법과 법률이 정한 법관에\n" +
                "의하여 법률에 의한 재판을 받을 권리를 가진다.\n" +
                " 가부동수인 때에는 부결된 것으로 본다.\n" +
                "행정권은 대통령을 수반으로 하는 정부에\n" +
                "속한다." + FlipContentSeparator.separator +
                "손금의 정확도는 50%~60%정도 입니다. 얼마전 손금에 대하여 방영이 되고난후 손금에 대한 관심이 부쩍 높아진게 사실입니다. 그러나 손금은 어느일부분의 선만 가지고 이야기 하면 정확히 볼 수 없습니다."
                + FlipContentSeparator.separator +
                "행정권은 대통령을 수반으로 하는 정부에 속한다. 모든 국민은 헌법과 법률이 정한 법관에 의하여 법률에 의한 재판을 받을 권리를 가진다. 모든 국민의 재산권은 보장된다.",
        liked = false,
        likeCnt = 302,
        commentCnt = 28,
        scraped = false,
        bgColorType = BackgroundColorType.BLUE,
        createdAt = "2023.12.${if (it / 10 == 0) "0${it}" else it}"
    )
}