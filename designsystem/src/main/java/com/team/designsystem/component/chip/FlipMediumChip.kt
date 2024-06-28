package com.team.designsystem.component.chip

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.designsystem.R
import com.team.designsystem.component.utils.clickableSingle
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme

@Composable
fun FlipMediumChip(
    modifier: Modifier = Modifier,
    text: String,
    @DrawableRes icon: Int,
    solid: Boolean = true,
    onClick: () -> Unit
) {

    Box(
        modifier = modifier
            .wrapContentSize()
            .clip(FlipTheme.shapes.roundedCornerSmall)
            .background(if (solid) FlipTheme.colors.point else FlipTheme.colors.gray1)
            .clickableSingle { onClick() },
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(start = 12.dp, end = 16.dp, top = 5.dp, bottom = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp, alignment = Alignment.CenterHorizontally)
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = ImageVector.vectorResource(icon),
                contentDescription = text,
                tint = if (solid) FlipTheme.colors.white else FlipTheme.colors.gray6
            )
            Text(
                modifier = Modifier,
                text = text,
                style = FlipTheme.typography.body5,
                color = if (solid) FlipTheme.colors.white else FlipTheme.colors.gray6
            )
        }
    }
}

@Preview(name = "solid", showBackground = true)
@Composable
private fun FlipMediumChipPreview() {
    FlipAppTheme {
        FlipMediumChip(
            text = "Text",
            icon = R.drawable.ic_outlined_setting,
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
            icon = R.drawable.ic_outlined_setting,
            solid = false,
            onClick = {}
        )
    }
}