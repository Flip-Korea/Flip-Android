package com.team.presentation.flip.component

import androidx.compose.ui.graphics.Color
import com.team.presentation.R

/**
 * Flip Fab 아이템
 *
 * @param outlinedIcon 아이템 OutLined 아이콘 (= 비활성화 표시)
 * @param filledIcon 아이템 Filled 아이콘 (= 활성화 표시)
 * @param title 아이템 제목
 * @param color 아이템 색상
 * @param switchable 아이템 클릭 여부
 * @param fabEvent Fab 이벤트 [FabEvent]
 */
sealed class FabItem(
    val outlinedIcon: Int,
    val filledIcon: Int,
    val title: Int,
    val color: Color,
    val switchable: Boolean,
    val fabEvent: FabEvent
) {

    data object Like: FabItem(
        outlinedIcon = R.drawable.ic_outlined_like,
        filledIcon = R.drawable.ic_filled_like,
        title = R.string.icon_button_like,
        color = Color(0xFFFF1F4B),
        switchable = true,
        fabEvent = FabEvent.OnLikeClick
    )

    data object Scrap: FabItem(
        outlinedIcon = R.drawable.ic_outlined_scrap,
        filledIcon = R.drawable.ic_filled_scrap,
        title = R.string.icon_button_scrap,
        color = Color(0xFF4990FF),
        switchable = true,
        fabEvent = FabEvent.OnScrapClick
    )

    data object Comment: FabItem(
        outlinedIcon = R.drawable.ic_outlined_comment,
        filledIcon = R.drawable.ic_outlined_comment,
        title = R.string.icon_button_comment,
        color = Color(0xFF00AB85),
        switchable = false,
        fabEvent = FabEvent.OnCommentClick
    )

    data object More: FabItem(
        outlinedIcon = R.drawable.ic_more,
        filledIcon = R.drawable.ic_more,
        title = R.string.icon_button_more,
        color = Color.White,
        switchable = false,
        fabEvent = FabEvent.OnMoreClick
    )
}

/** Flip Fab 아이템 리스트 */
val fabItems = listOf(
    FabItem.Like,
    FabItem.Scrap,
    FabItem.Comment,
    FabItem.More,
)