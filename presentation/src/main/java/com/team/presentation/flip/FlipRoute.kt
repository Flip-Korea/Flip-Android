package com.team.presentation.flip

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.team.designsystem.theme.FlipTheme
import com.team.domain.model.post.Post
import com.team.domain.model.profile.DisplayProfile
import com.team.domain.type.BackgroundColorType
import com.team.domain.type.FlipContentSeparator
import com.team.presentation.flip.view.FlipShortFormScreen

@Composable
fun FlipRoute() {

    FlipShortFormScreen(
        modifier = Modifier.background(color = FlipTheme.colors.white),
        posts = PostsTestData
    )
}

private val PostsTestData = List(15) {
    Post(
        postId = it.toLong(),
        profile = DisplayProfile(profileId = "profileId", nickname = "홍길동", photoUrl = "https://images.dog.ceo/breeds/bulldog-boston/n02096585_11731.jpg"),
        title = "행정권은 대통령을 수반으로 하는 정부에 속한다.",
        content = "가부동수인 때에는 부결된 것으로 본다.\n" +
                "행정권은 대통령을 수반으로 하는 정부에\n" +
                "속한다. 모든 국민은 헌법과 법률이 정한 법관에\n" +
                "의하여 법률에 의한 재판을 받을 권리를 가진다.\n" +
                " 가부동수인 때에는 부결된 것으로 본다.\n" +
                "행정권은 대통령을 수반으로 하는 정부에\n" +
                "속한다." + FlipContentSeparator.SEPARATOR +
                "손금의 정확도는 50%~60%정도 입니다. 얼마전 손금에 대하여 방영이 되고난후 손금에 대한 관심이 부쩍 높아진게 사실입니다. 그러나 손금은 어느일부분의 선만 가지고 이야기 하면 정확히 볼 수 없습니다."
                + FlipContentSeparator.SEPARATOR +
                "행정권은 대통령을 수반으로 하는 정부에 속한다. 모든 국민은 헌법과 법률이 정한 법관에 의하여 법률에 의한 재판을 받을 권리를 가진다. 모든 국민의 재산권은 보장된다.",
        liked = it % 2 == 0,
        likeCnt = 302,
        commentCnt = 28,
        scraped = false,
        bgColorType = BackgroundColorType.entries.random(),
        createdAt = "2023.12.${if (it / 10 == 0) "0${it}" else it}"
    )
}