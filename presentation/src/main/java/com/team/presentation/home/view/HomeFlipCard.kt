package com.team.presentation.home.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.team.designsystem.component.button.FlipIconButton
import com.team.designsystem.component.utils.clickableSingle
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme
import com.team.domain.model.post.Post
import com.team.domain.model.profile.DisplayProfile
import com.team.presentation.R
import com.team.presentation.home.FlipCardUiEvent
import com.team.presentation.home.util.FlipCardTokens

/**
 * Flip Card
 *
 * Flip의 일부 정보가 보여지는 CardView 형태이다.
 * @param post Flip 카드에 필요한 post 정보가 담겨있다. (flip == post)
 * @param flipCardUiEvent Flip 카드에 대한 UiEvent 이다.
 */
@Composable
fun HomeFlipCard(
    modifier: Modifier = Modifier,
    post: Post, //TODO Post 말고 보여지는 정보만 가지고 있는 객체 따로 생성
    flipCardUiEvent: (FlipCardUiEvent) -> Unit,
) {

    Box(
       modifier = modifier
           .clip(FlipTheme.shapes.roundedCornerFlipCard)
           .background(FlipCardTokens.bgColorMap(post.bgColorId))
           .clickableSingle { flipCardUiEvent(FlipCardUiEvent.OnFlipCardClick) },
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 16.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.CenterVertically)
        ) {
            CardTopSection(
                modifier = Modifier.fillMaxWidth(),
                photoUrl = post.profile.photoUrl,
                nickname = post.profile.nickname,
                profileId = post.profile.profileId,
                onMoreClick = { flipCardUiEvent(FlipCardUiEvent.OnMoreClick) }
            )
            CardMiddleSection(
                modifier = Modifier.fillMaxWidth(),
                title = post.title,
                content = post.content
            )
            CardBottomSection(
                modifier = Modifier.fillMaxWidth(),
                createdAt = post.createdAt,
                liked = post.liked,
                likeCnt = post.likeCnt,
                commentCnt = post.commentCnt,
                scraped = post.scraped,
                uiEvent = { uiEvent -> flipCardUiEvent(uiEvent) }
            )
        }
    }
}

@Composable
private fun CardTopSection(
    modifier: Modifier = Modifier,
    photoUrl: String,
    nickname: String,
    profileId: String,
    onMoreClick: () -> Unit,
) {

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(8.62.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                model = photoUrl,
                contentDescription = "${nickname}의 ${stringResource(id = R.string.home_flip_card_content_desc_photo_url)}",
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.ic_logo_dark)
            )
            Column {
                Text(text = nickname, style = FlipTheme.typography.headline1)
                Text(
                    text = profileId,
                    style = FlipTheme.typography.body1,
                    color = FlipTheme.colors.gray6,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        FlipIconButton(
            modifier = Modifier.size(24.dp),
            imageVector = ImageVector.vectorResource(R.drawable.ic_more),
            contentDescription = stringResource(id = R.string.home_flip_card_content_desc_more),
            tint = FlipTheme.colors.main,
            onClick = onMoreClick
        )
    }
}

@Composable
private fun CardMiddleSection(
    modifier: Modifier = Modifier,
    title: String,
    content: String,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.Top)
    ) {
        Text(
            text = title,
            style = FlipTheme.typography.headline3,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = content,
            style = FlipTheme.typography.body5,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
//            textAlign = TextAlign.Justify
        )
    }
}

@Composable
private fun CardBottomSection(
    modifier: Modifier = Modifier,
    createdAt: String,
    liked: Boolean,
    likeCnt: Long,
    commentCnt: Long,
    scraped: Boolean,
    uiEvent: (FlipCardUiEvent) -> Unit,
) {

    var likedClicked by rememberSaveable { mutableStateOf(false) }
    var scrapClicked by rememberSaveable { mutableStateOf(false) }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = createdAt, style = FlipTheme.typography.body3, color = FlipTheme.colors.gray5)
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .clip(FlipTheme.shapes.roundedCornerFlipCard)
                    .clickableSingle {
                        uiEvent(FlipCardUiEvent.OnLikeClick)
                        likedClicked = !likedClicked
                    },
                horizontalArrangement = Arrangement.spacedBy(1.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = ImageVector.vectorResource(
                        if (liked || likedClicked) {
                            R.drawable.ic_filled_like
                        } else R.drawable.ic_outlined_like
                    ),
                    contentDescription = stringResource(id = R.string.icon_button_like),
                    tint = Color(0xFFFF1F4B)
                )
                Text(
                    text = likeCnt.toString(),
                    style = FlipTheme.typography.body3,
                    color = FlipTheme.colors.gray6
                )
            }

            Row(
                modifier = Modifier
                    .clip(FlipTheme.shapes.roundedCornerFlipCard)
                    .clickableSingle { uiEvent(FlipCardUiEvent.OnCommentClick) },
                horizontalArrangement = Arrangement.spacedBy(1.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.ic_outlined_comment),
                    contentDescription = stringResource(id = R.string.icon_button_comment),
                    tint = Color(0xFF212121)
                )
                Text(
                    text = commentCnt.toString(),
                    style = FlipTheme.typography.body3,
                    color = FlipTheme.colors.gray6
                )
            }

            FlipIconButton(
                modifier = Modifier.size(24.dp),
                imageVector = ImageVector.vectorResource(
                    if (scraped || scrapClicked) {
                        R.drawable.ic_filled_scrap
                    } else R.drawable.ic_outlined_scrap
                ),
                contentDescription = stringResource(id = R.string.icon_button_scrap),
                tint = Color(0xFF212121),
                onClick = {
                    uiEvent(FlipCardUiEvent.OnScrapClick)
                    scrapClicked = !scrapClicked
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CardTopSectionPreview() {
    FlipAppTheme {
        CardTopSection(
            photoUrl = "",
            nickname = "어스름늑대",
            profileId = "90WXYZ6789A1B2C3",
            onMoreClick = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CardMiddleSectionPreview() {
    FlipAppTheme {
        CardMiddleSection(
            title = "행정권은 대통령을 수반으로 어쩌고 어쩌고!",
            content = "행정권은 대통령을 수반으로 하는 정부에\n" +
                    "속한다. 모든 국민은 헌법과 법률이 정한 법관에\n" +
                    "의하여 법률에 의한 재판을 받을 권리를 가진다.행정권은 대통령을 수반으로 하는 정부에\n" +
                    "속한다. 모든 국민은 헌법과 법률이 정한 법관에\n" +
                    "의하여 법률에 의한 재판을 받을 권리를 가진다."
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CardBottomSectionPreview() {
    FlipAppTheme {
        CardBottomSection(
            modifier = Modifier.fillMaxWidth(),
            createdAt = "2024.01.24",
            liked = false,
            likeCnt = 78,
            commentCnt = 21,
            scraped = false,
            uiEvent = { }
        )
    }
}

@Preview()
@Composable
private fun HomeFlipCardPreview() {
    FlipAppTheme {
        HomeFlipCard(
            post = Post(
                postId = 0L,
                profile = DisplayProfile(
                    nickname = "어스름늑대",
                    profileId = "90WXYZ6789A1B2C3",
                    photoUrl = ""
                ),
                title = "행정권은 대통령을 수반으로 어쩌고 어쩌고!",
                content = "행정권은 대통령을 수반으로 하는 정부에\n" +
                        "속한다. 모든 국민은 헌법과 법률이 정한 법관에\n" +
                        "의하여 법률에 의한 재판을 받을 권리를 가진다.행정권은 대통령을 수반으로 하는 정부에\n" +
                        "속한다. 모든 국민은 헌법과 법률이 정한 법관에\n" +
                        "의하여 법률에 의한 재판을 받을 권리를 가진다.",
                createdAt = "2024.01.24",
                liked = false,
                likeCnt = 78,
                commentCnt = 21,
                scraped = false,
                bgColorId = 2
            ),
            flipCardUiEvent = { }
        )
    }
}