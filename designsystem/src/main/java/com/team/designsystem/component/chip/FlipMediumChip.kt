package com.team.designsystem.component.chip

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.designsystem.component.utils.clickableSingle
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme

@Composable
fun FlipMediumChip(
    modifier: Modifier = Modifier,
    text: String,
    solid: Boolean = true,
    onClick: () -> Unit
) {

    Box(
        modifier = modifier
            .clip(FlipTheme.shapes.roundedCornerExtraLarge)
            .wrapContentSize()
            .background(if (solid) FlipTheme.colors.point else FlipTheme.colors.gray1)
            .clickableSingle { onClick() }
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 20.dp, vertical = 6.dp),
            text = text,
            style = FlipTheme.typography.body5,
            color = if (solid) FlipTheme.colors.white else FlipTheme.colors.gray6
        )
    }
}

@Preview(name = "solid", showBackground = true)
@Composable
private fun FlipMediumChipPreview() {
    FlipAppTheme {
        FlipMediumChip(
            text = "Text",
            onClick = {}
        )
    }
}

@Preview(name = "not solid(tinted)", showBackground = true)
@Composable
private fun FlipMediumChip2Preview() {
    FlipAppTheme {
        FlipMediumChip(
            text = "Text",
            solid = false,
            onClick = {}
        )
    }
}