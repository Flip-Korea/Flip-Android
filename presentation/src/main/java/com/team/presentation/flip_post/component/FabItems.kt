package com.team.presentation.flip_post.component

import androidx.compose.ui.graphics.Color
import com.team.designsystem.R

data class FabItem(
    val outlinedIcon: Int,
    val filledIcon: Int,
    val title: Int,
    val color: Color,
    val active: Boolean
)

val fabItems = listOf(
    FabItem(
        R.drawable.ic_outlined_like,
        R.drawable.ic_filled_like,
        R.string.fab_item_like,
        Color(0xFFFF1F4B),
        false
    ),
    FabItem(
        R.drawable.ic_outlined_scrap,
        R.drawable.ic_filled_scrap,
        R.string.fab_item_scrap,
        Color(0xFF4990FF),
        false
    ),
    FabItem(
        R.drawable.ic_outlined_comment,
        R.drawable.ic_outlined_comment,
        R.string.fab_item_comment,
        Color(0xFF00AB85),
        false
    ),
    FabItem(
        R.drawable.ic_share,
        R.drawable.ic_share,
        R.string.fab_item_share,
        Color.White,
        false
    ),
    FabItem(
        R.drawable.ic_more,
        R.drawable.ic_more,
        R.string.fab_item_more,
        Color.White,
        false
    ),
)