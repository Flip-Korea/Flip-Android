package com.team.presentation.flip.component

import androidx.compose.ui.graphics.Color
import com.team.presentation.R

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
        R.string.icon_button_like,
        Color(0xFFFF1F4B),
        false
    ),
    FabItem(
        R.drawable.ic_outlined_scrap,
        R.drawable.ic_filled_scrap,
        R.string.icon_button_scrap,
        Color(0xFF4990FF),
        false
    ),
    FabItem(
        R.drawable.ic_outlined_comment,
        R.drawable.ic_outlined_comment,
        R.string.icon_button_comment,
        Color(0xFF00AB85),
        false
    ),
    FabItem(
        R.drawable.ic_share,
        R.drawable.ic_share,
        R.string.icon_button_share,
        Color.White,
        false
    ),
    FabItem(
        R.drawable.ic_more,
        R.drawable.ic_more,
        R.string.icon_button_more,
        Color.White,
        false
    ),
)