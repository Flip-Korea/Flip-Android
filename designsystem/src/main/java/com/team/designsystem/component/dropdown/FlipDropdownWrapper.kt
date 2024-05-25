package com.team.designsystem.component.dropdown

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.team.designsystem.theme.FlipTheme

private fun Modifier.crop(
    horizontal: Dp = 0.dp,
    vertical: Dp = 0.dp,
): Modifier = this.layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)
    fun Dp.toPxInt(): Int = this.toPx().toInt()

    layout(
        placeable.width - (horizontal * 2).toPxInt(),
        placeable.height - (vertical * 2).toPxInt()
    ) {
        placeable.placeRelative(-horizontal.toPx().toInt(), -vertical.toPx().toInt())
    }
}

@Composable
fun FlipDropdownWrapper(
    modifier: Modifier = Modifier,
    space: Dp = 2.dp,
    button: @Composable () -> Unit,
    menu: @Composable (Modifier, DpOffset) -> Unit,
) {
    var offset by remember { mutableStateOf(DpOffset.Zero) }

    Box(modifier = modifier.wrapContentSize()) {
        button()
        menu(
            Modifier
                .wrapContentSize()
//                .crop(vertical = 8.dp)
                .background(FlipTheme.colors.white),
            offset.copy(y = offset.y + space)
        )
    }
}