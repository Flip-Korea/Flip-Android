package com.team.presentation.flip.view

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.team.designsystem.component.button.FlipFollowButton
import com.team.designsystem.component.button.FlipFollowButtonSize
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme
import com.team.domain.model.post.Post
import com.team.domain.model.profile.DisplayProfile
import com.team.domain.type.FlipContentSeparator
import com.team.presentation.R
import com.team.presentation.common.util.CommonPaddingValues
import com.team.presentation.flip.component.FlipFab

/**
 * Flip(ShortForm) 단일 화면
 *
 * @param post 플립(Post)
 */
@Composable
fun FlipScreen(
    modifier: Modifier = Modifier,
    post: Post,
) {

    var screenWidth by remember { mutableIntStateOf(0) }

    var isFabExpanded by remember { mutableStateOf(false) }

    /** 컨텐츠(본문) 값들 */
    //TODO: split 연산을 여기서 하지말고 애초에 Post 모델 대신 ComposePost 같은 프레젠테이션 용 모델 생성해서 사용하기
    val contents by rememberSaveable { mutableStateOf(post.content.split(FlipContentSeparator.separator)) }
    var currentPage by rememberSaveable { mutableIntStateOf(0) }
    var content by rememberSaveable { mutableStateOf(post.content.split(FlipContentSeparator.separator)[0]) }
    LaunchedEffect(currentPage) { content = contents[currentPage] }

    Box(
        modifier = modifier
            .fillMaxSize()
            .onSizeChanged { screenWidth = it.width }
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 24.dp + CommonPaddingValues.TopBarVertical
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            /** 타이틀 등이 포함된 상단 영역 */
            TopSection(
                modifier = Modifier.fillMaxWidth(),
                title = post.title,
                createdAt = post.createdAt,
                liked = post.liked,
                likeCnt = post.likeCnt,
                commentCnt = post.commentCnt
            )
            /** 컨텐츠(본문) 영역 */
            ContentSection(
                modifier = Modifier.weight(1f),
                content = content,
                onTap = { offset ->
                    currentPage = if (offset.x < screenWidth / 2) {
                        (currentPage - 1).coerceIn(contents.indices)
                    } else {
                        (currentPage + 1).coerceIn(contents.indices)
                    }
                }
            )
            /** 작성자의 정보 및 드롭다운 등이 포함된 하단 영역 (+ 현재 페이지 위치) */
            BottomSection(
                modifier = Modifier.padding(bottom = 4.dp),
                imageUrl = "",
                nickname = "코딩마스터",
                profileId = "@90WXYZ6789A1B2C3",
                isFollowing = false,
                isFollower = false,
                currentPage = currentPage,
                onFollowButtonClick = { },
            )
        }

        /** Fab Button (간격 수동 기입) */
        FlipFab(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 24.dp, end = 16.dp),
            isExpanded = isFabExpanded,
            changeExpanded = { isFabExpanded = !isFabExpanded },
            onDismissRequest = { isFabExpanded = false }
        )
    }
}

/** 상단 영역 */
@Composable
private fun TopSection(
    modifier: Modifier = Modifier,
    title: String,
    createdAt: String,
    liked: Boolean,
    likeCnt: Long,
    commentCnt: Long,
) {
    Column(modifier = modifier) {

        Text(text = title, style = FlipTheme.typography.headline3)
        Spacer(modifier = Modifier.size(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = createdAt, style = FlipTheme.typography.body3, color = FlipTheme.colors.gray5)

            Row {

                /** 좋아요 */
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = ImageVector.vectorResource(
                            if (!liked) R.drawable.ic_outlined_post_like else R.drawable.ic_filled_post_like
                        ),
                        contentDescription = null,
                        tint = likeIconColor
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(text = "$likeCnt", style = FlipTheme.typography.body3)
                }
                Spacer(modifier = Modifier.size(8.dp))

                /** 댓글 */
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = ImageVector.vectorResource(R.drawable.ic_outlined_post_comment),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(text = "$commentCnt", style = FlipTheme.typography.body3)
                }
            }
        }
    }
}

/** 본문 영역 */
@Composable
private fun ContentSection(
    modifier: Modifier = Modifier,
    content: String,
    onTap: (Offset) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { offset -> onTap(offset) }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = content,
            style = FlipTheme.typography.body7,
            textAlign = TextAlign.Center
        )
    }
}

/** 하단 영역 */
@Composable
private fun BottomSection(
    modifier: Modifier = Modifier,
    imageUrl: String,
    nickname: String,
    profileId: String,
    isFollowing: Boolean,
    isFollower: Boolean,
    currentPage: Int,
    onFollowButtonClick: () -> Unit,
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            ProfileSection(
                imageUrl = imageUrl,
                nickname = nickname,
                profileId = profileId,
                isFollowing = isFollowing,
                isFollower = isFollower,
                onFollowButtonClick = onFollowButtonClick
            )
        }

        /** 페이지 진행률 바 */
        FlipPagesProgressBar(currentPage = currentPage)
    }
}

@Composable
private fun ProfileSection(
    modifier: Modifier = Modifier,
    imageUrl: String,
    nickname: String,
    profileId: String,
    isFollowing: Boolean,
    isFollower: Boolean,
    onFollowButtonClick: () -> Unit
) {
    val context = LocalContext.current
    val imageRequest = remember {
        ImageRequest.Builder(context)
            .data(imageUrl)
            .placeholder(R.drawable.ic_logo_dark)
            .error(R.drawable.ic_logo_dark)
            .build()
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .clip(CircleShape)
                .size(40.dp),
            model = imageRequest,
            contentDescription = null,
            contentScale = ContentScale.Fit,
        )
        Column {
            Text(text = nickname, style = FlipTheme.typography.body5)
            Text(text = profileId, style = FlipTheme.typography.body1, color = FlipTheme.colors.gray6)
        }
        FlipFollowButton(
            modifier = Modifier.padding(start = 4.dp),
            size = FlipFollowButtonSize.Small2,
            isFollowing = isFollowing,
            isFollower = isFollower,
            onClick = onFollowButtonClick
        )
    }
}

private val likeIconColor = Color(0xFFFF1F4B)

@Preview(showBackground = true)
@Composable
private fun TopSectionPreview() {
    FlipAppTheme {
        TopSection(
            modifier = Modifier.fillMaxWidth(),
            title = PostTestData.title,
            createdAt = PostTestData.createdAt,
            liked = PostTestData.liked,
            likeCnt = PostTestData.likeCnt,
            commentCnt = PostTestData.commentCnt,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileSectionPreview() {
    FlipAppTheme {
        ProfileSection(
            modifier = Modifier.fillMaxWidth(),
            imageUrl = "",
            nickname = "코딩마스터",
            profileId = "@90WXYZ6789A1B2C3",
            isFollowing = false,
            isFollower = false,
            onFollowButtonClick = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomSectionPreview() {

    var isFabExpanded by remember {
        mutableStateOf(false)
    }

    FlipAppTheme {
        BottomSection(
            imageUrl = "",
            nickname = "코딩마스터",
            profileId = "@90WXYZ6789A1B2C3",
            isFollowing = false,
            isFollower = false,
            currentPage = 1,
            onFollowButtonClick = { },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FlipScreenPreview() {

    FlipAppTheme {
        FlipScreen(post = PostTestData)
    }
}

private val PostTestData = Post(
    postId = 1,
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
    bgColorId = 3,
    createdAt = "2023.12.26"
)