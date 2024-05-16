package com.team.designsystem.component.dropdown

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.team.designsystem.R
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipNoRipple
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

/**
 * Must be used with IconButton
 * IconButton size is fixed to 24.dp
 */
@Composable
fun FlipDropDownMenu(
    modifier: Modifier = Modifier,
    @DrawableRes iconRes: Int,
    expanded: Boolean,
    dropDownItems: List<DropDownItem>,
    onClick: () -> Unit,
    onItemClick: (DropDownItem) -> Unit,
    onDismissRequest: () -> Unit = {},
) {

//    var expanded by rememberSaveable { mutableStateOf(false) }
    var offset by remember { mutableStateOf(DpOffset.Zero) }
    var itemHeight by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    Box(
        modifier = modifier.wrapContentSize(),
//        contentAlignment = Alignment.TopStart
    ) {

        CompositionLocalProvider(LocalRippleTheme provides FlipNoRipple()) {
            IconButton(
                modifier = Modifier
                    .size(24.dp)
                    .onSizeChanged { itemHeight = with(density) { it.height.toDp() } } ,
                onClick = onClick
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(iconRes),
                    contentDescription = stringResource(id = R.string.content_desc_menu_more)
                )
            }
        }

        DropdownMenu(
            modifier = Modifier
                .clip(FlipTheme.shapes.roundedCornerMedium)
                .wrapContentSize()
                .crop(vertical = 8.dp)
                .background(FlipTheme.colors.white),
            expanded = expanded,
            onDismissRequest = onDismissRequest,
            offset = offset.copy(y = offset.y + 8.dp)
        ) {
            dropDownItems.forEachIndexed { index, item ->
                DropdownMenuItem(
                    text = { Text(text = item.text, style = FlipTheme.typography.body5) },
                    onClick = { onItemClick(item) },
                )
                if (index != dropDownItems.size - 1) {
                    HorizontalDivider(thickness = 1.dp, color = FlipTheme.colors.gray2)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FlipDropDownMenuPreview() {

    val dropDownItems = listOf(
        DropDownItem(0, "sample 1"),
        DropDownItem(1, "sample 2"),
        DropDownItem(2, "sample 3"),
    )

    var expanded by rememberSaveable { mutableStateOf(false) }

    FlipAppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            FlipDropDownMenu(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp),
                iconRes = R.drawable.ic_more,
                expanded = expanded,
                dropDownItems = dropDownItems,
                onClick = { expanded = true },
                onItemClick = { },
                onDismissRequest = { expanded = false },
            )
        }
    }
}