package com.team.designsystem.component.dropdown

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.designsystem.R
import com.team.designsystem.component.TouchTarget
import com.team.designsystem.theme.FlipTheme

@Composable
fun FlipRadioButton(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: () -> Unit,
    enabled: Boolean = true,
) {
    IconToggleButton(
        modifier = modifier.size(24.dp),
        checked = checked,
        onCheckedChange = { onCheckedChange() },
    ) {
        Icon(
            modifier = Modifier
                .size(24.dp)
                .border(
                    1.dp,
                    if (checked) {
                        FlipTheme.colors.main
                    } else if (!enabled) {
                        FlipTheme.colors.gray3
                    } else FlipTheme.colors.gray5,
                    CircleShape
                ),
            painter = painterResource(id = R.drawable.ic_empty_circle_checkbox),
            contentDescription = stringResource(id = R.string.content_desc_check),
            tint = Color.Transparent,
        )
        if (checked) {
            Icon(
                modifier = Modifier.size(12.dp),
                painter = painterResource(id = R.drawable.ic_inner_circle_checkbox),
                contentDescription = stringResource(id = R.string.content_desc_check),
                tint = FlipTheme.colors.main,
            )
        }
    }
}

@Preview(name = "TouchTarget O")
@Composable
fun FlipRadioButtonPreview() {
    var checked by remember { mutableStateOf(false) }

    FlipRadioButton(
        modifier = Modifier
            .padding(TouchTarget.padding)
            .size(24.dp),
        checked = checked,
        onCheckedChange = { checked = !checked }
    )
}

@Preview(name = "TouchTarget X")
@Composable
fun FlipRadioButtonPreview2() {
    var checked by remember { mutableStateOf(false) }

    FlipRadioButton(
        modifier = Modifier.size(24.dp),
        checked = checked,
        onCheckedChange = { checked = !checked }
    )
}