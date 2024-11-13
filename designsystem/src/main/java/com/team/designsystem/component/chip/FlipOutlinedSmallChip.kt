package com.team.designsystem.component.chip

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.designsystem.R
import com.team.designsystem.component.utils.ClickableSingle
import com.team.designsystem.component.utils.clickableSingle
import com.team.designsystem.component.utils.get
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme

@Composable
fun FlipOutlinedSmallChip(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit = {},
) {

    val clickableSingle = remember { ClickableSingle.get() }

    Box(
        modifier = modifier
            .clip(FlipTheme.shapes.roundedCornerExtraLarge)
            .border(
                width = 1.dp,
                color = FlipTheme.colors.gray4,
                shape = FlipTheme.shapes.roundedCornerExtraLarge
            )
            .wrapContentSize()
            .background(FlipTheme.colors.white)
            .clickableSingle { clickableSingle.onEvent(onClick) }
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(start = 20.dp, end = 12.dp, top = 4.5.dp, bottom = 4.5.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp, alignment = Alignment.Start),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                style = FlipTheme.typography.body5,
                color = FlipTheme.colors.gray7
            )
            Icon(
                modifier = Modifier
                    .size(24.dp),
                imageVector = ImageVector.vectorResource(R.drawable.ic_delete),
                contentDescription = stringResource(id = R.string.content_desc_delete),
                tint = FlipTheme.colors.gray7
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FlipOutlinedSmallChipPreview() {
    FlipAppTheme {
        FlipOutlinedSmallChip(
            text = "Text",
            onClick = {},
        )
    }
}