package com.team.designsystem.component.button

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.designsystem.R
import com.team.designsystem.component.utils.ClickableSingle
import com.team.designsystem.component.utils.get
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme

/**
 * 1. Icon Size는 최소 24dp
 * 2. Button, TextField 같은 작은 요소 안에서 보다 큼직한 요소 및 외부에서 쓰임
 * 3. TouchTarget: 44dp
 */
@Composable
fun FlipIconButton(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    contentDescription: String?,
    tint: Color,
    onClick: () -> Unit,
) {

    val clickableSingle = remember { ClickableSingle.get() }

    IconButton(
        modifier = modifier.size(44.dp),
        onClick = { clickableSingle.onEvent(onClick) }
    ) {
        Icon(
            modifier = Modifier.sizeIn(minWidth = 24.dp, minHeight = 24.dp),
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = tint
        )
    }
}

/**
 * 1. Size는 최소 24dp
 * 2. Button, TextField 같은 작은 요소 안에서 보다 큼직한 요소 및 외부에서 쓰임
 */
@Composable
fun FlipIconButton(
    modifier: Modifier = Modifier,
    painter: Painter,
    contentDescription: String?,
    tint: Color = LocalContentColor.current,
    onClick: () -> Unit,
) {

    val clickableSingle = remember { ClickableSingle.get() }

    IconButton(
        modifier = modifier.size(44.dp),
        onClick = { clickableSingle.onEvent(onClick) }
    ) {
        Icon(
            modifier = Modifier.sizeIn(minWidth = 24.dp, minHeight = 24.dp),
            painter = painter,
            contentDescription = contentDescription,
            tint = tint
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FlipIconButtonPreview() {
    FlipAppTheme {
//        Box(modifier = Modifier.size(100.dp)) {
//        }
        FlipIconButton(
            modifier = Modifier,
            imageVector = Icons.Outlined.Settings,
            contentDescription = null,
            onClick = { },
            tint = FlipTheme.colors.main
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FlipIconButtonPreview2() {

    var clicked by remember { mutableStateOf(false) }

    FlipAppTheme {
        Row {
            FlipIconButton(
                painter = painterResource(id = R.drawable.ic_outlined_setting),
                contentDescription = null,
                onClick = { clicked = !clicked },
                tint = if (clicked) {
                    Color.Red
                } else FlipTheme.colors.main
            )
            FlipIconButton(
                imageVector = ImageVector.vectorResource(R.drawable.ic_outlined_setting),
                contentDescription = null,
                onClick = { clicked = !clicked },
                tint = if (clicked) {
                    Color.Red
                } else FlipTheme.colors.main
            )
        }
    }
}