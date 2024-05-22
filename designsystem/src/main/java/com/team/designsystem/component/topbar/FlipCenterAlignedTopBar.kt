package com.team.designsystem.component.topbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.designsystem.R
import com.team.designsystem.component.button.FlipIconButton
import com.team.designsystem.component.button.FlipTextButton
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme

@Composable
fun FlipCenterAlignedTopBar(
    modifier: Modifier = Modifier,
    title: String,
    onBackPress: (() -> Unit)? = null,
    options: @Composable (RowScope.() -> Unit)? = null
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 18.dp, bottom = 24.dp)
    ) {
        if (onBackPress != null) {
            FlipIconButton(
                modifier = Modifier.align(Alignment.CenterStart),
                imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_back),
                contentDescription = stringResource(id = R.string.content_desc_arrow_back),
                onClick = onBackPress,
                tint = FlipTheme.colors.main
            )
        }

        Text(
            modifier = Modifier.align(Alignment.Center),
            text = title,
            style = FlipTheme.typography.headline4,
            textAlign = TextAlign.Center
        )

        if (options != null) {
            Row(
                modifier = Modifier.align(Alignment.CenterEnd),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                content = options
            )
        }
    }
}

@Preview(name = "Top bar type2(타이틀 중앙 정렬) - 뒤로가기 버튼/화면이름", showBackground = true)
@Composable
private fun FlipCenterAlignedTopBarPreview() {
    FlipAppTheme {
        FlipCenterAlignedTopBar(title = "화면 이름", onBackPress = { })
    }
}

@Preview(name = "Top bar type2(타이틀 중앙 정렬) - 화면이름/아이콘 버튼", showBackground = true)
@Composable
private fun FlipCenterAlignedTopBarPreview2() {
    FlipAppTheme {
        FlipCenterAlignedTopBar(
            title = "화면 이름",
            options = {
                FlipIconButton(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_share_2),
                    contentDescription = null,
                    onClick = { },
                    tint = FlipTheme.colors.main
                )
                FlipIconButton(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_outlined_setting),
                    contentDescription = null,
                    onClick = { },
                    tint = FlipTheme.colors.main
                )
            }
        )
    }
}

@Preview(name = "Top bar type2(타이틀 중앙 정렬) - 뒤로가기 버튼/화면이름/버튼", showBackground = true)
@Composable
private fun FlipCenterAlignedTopBarPreview3() {
    FlipAppTheme {
        FlipCenterAlignedTopBar(
            title = "화면 이름",
            onBackPress = { },
            options = {
                FlipTextButton(text = "버튼 이름", onClick = { })
            }
        )
    }
}