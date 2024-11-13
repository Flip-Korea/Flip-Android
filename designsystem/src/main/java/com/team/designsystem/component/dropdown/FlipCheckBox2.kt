package com.team.designsystem.component.dropdown

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.designsystem.R
import com.team.designsystem.component.TouchTarget
import com.team.designsystem.theme.FlipTheme

@Composable
fun FlipCheckBox2(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onClick: () -> Unit,
    enabled: Boolean = true,

    ) {
    Box(
        modifier = modifier
            .border(
                1.dp,
                if (checked) {
                    FlipTheme.colors.point
                } else FlipTheme.colors.gray5,
                FlipTheme.shapes.roundedCornerSmall
            )
            .size(24.dp)
            .clip(FlipTheme.shapes.roundedCornerSmall)
            .background(
                if (checked) FlipTheme.colors.point else Color.Transparent,
                FlipTheme.shapes.roundedCornerSmall
            )
            .clickable(
                enabled = enabled,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Icon(
                modifier = Modifier,
                painter = painterResource(id = R.drawable.ic_check),
                contentDescription = stringResource(id = R.string.content_desc_check),
                tint = Color.White
            )
        }
    }
}

@Preview(name = "TouchTarget O")
@Composable
private fun FlipCheckBox2Preview() {

    var checked by remember { mutableStateOf(true) }

    FlipCheckBox2(
        modifier = Modifier
            .padding(TouchTarget.padding)
            .size(24.dp),
        checked = checked,
        onClick = { checked = !checked }
    )
}

@Preview(name = "TouchTarget X")
@Composable
private fun FlipCheckBox2Preview2() {

    var checked by remember { mutableStateOf(true) }

    FlipCheckBox2(
        modifier = Modifier.size(24.dp),
        checked = checked,
        onClick = { checked = !checked }
    )
}