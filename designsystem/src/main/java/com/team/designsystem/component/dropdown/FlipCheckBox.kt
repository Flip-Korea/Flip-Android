package com.team.designsystem.component.dropdown

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
fun FlipCheckBox(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .border(
                1.dp,
                if (checked) FlipTheme.colors.main else FlipTheme.colors.gray5,
                CircleShape
            )
            .size(24.dp)
            .clip(CircleShape)
            .background(if (checked) FlipTheme.colors.main else Color.Transparent, CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Icon(
                painter = painterResource(id = R.drawable.ic_check),
                contentDescription = stringResource(id = R.string.content_desc_check),
                tint = Color.White,
            )
        }
    }
}

@Preview(name = "TouchTarget O")
@Composable
private fun FlipCheckBoxPreview() {

    var checked by remember { mutableStateOf(false) }

    FlipCheckBox(
        modifier = Modifier
            .padding(TouchTarget.padding)
            .size(24.dp),
        checked = checked,
        onClick = { checked = !checked }
    )
}

@Preview(name = "TouchTarget X")
@Composable
private fun FlipCheckBoxPreview2() {

    var checked by remember { mutableStateOf(false) }

    FlipCheckBox(
        modifier = Modifier.size(24.dp),
        checked = checked,
        onClick = { checked = !checked }
    )
}