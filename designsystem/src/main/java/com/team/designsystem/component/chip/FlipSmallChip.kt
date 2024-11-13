package com.team.designsystem.component.chip

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.designsystem.R
import com.team.designsystem.component.utils.clickableSingle
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme

@Composable
fun FlipSmallChip(
    modifier: Modifier = Modifier,
    text: String,
    lightSolid: Boolean = true,
    deletable: Boolean = false,
    onClick: () -> Unit = {},
    onDelete: () -> Unit = {},
) {

    Box(
        modifier = modifier
            .clip(FlipTheme.shapes.roundedCornerExtraLarge)
            .border(
                width = 1.dp,
                color = if (lightSolid) FlipTheme.colors.point2 else Color.Transparent,
                shape = FlipTheme.shapes.roundedCornerExtraLarge
            )
            .wrapContentSize()
            .background(
                if (lightSolid) {
                    FlipTheme.colors.point3
                } else FlipTheme.colors.gray1
            )
            .clickableSingle { onClick() }
    ) {
        Row(
            modifier = Modifier
                .padding(
                    start = 20.dp,
                    end = if (deletable) 12.dp else 20.dp,
                    top = if (deletable) 4.5.dp else 6.dp,
                    bottom = if (deletable) 4.5.dp else 6.dp
                ),
            horizontalArrangement = Arrangement.spacedBy(4.dp, alignment = Alignment.Start),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                style = FlipTheme.typography.body5,
            )
            if (deletable) {
                Icon(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(24.dp)
                        .clickableSingle { onDelete() },
                    imageVector = ImageVector.vectorResource(R.drawable.ic_delete),
                    contentDescription = stringResource(id = R.string.content_desc_delete),
                    tint = FlipTheme.colors.main
                )
            }
        }
    }
}

@Preview(name = "lightSolid", showBackground = true)
@Composable
private fun FlipSmallChipPreview() {
    FlipAppTheme {
        FlipSmallChip(text = "Text")
    }
}

@Preview(name = "not lightSolid(tinted)", showBackground = true)
@Composable
private fun FlipSmallChip2Preview() {
    FlipAppTheme {
        FlipSmallChip(text = "Text", lightSolid = false)
    }
}

@Preview(name = "deletable", showBackground = true)
@Composable
private fun FlipSmallChip3Preview() {
    FlipAppTheme {
        FlipSmallChip(text = "Text", deletable = true, onDelete = {})
    }
}