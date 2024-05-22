package com.team.designsystem.component.topbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
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

/**
 * @param option 사용 시 modifier 연결해서 사용
 */
@Composable
fun FlipTopBar(
    modifier: Modifier = Modifier,
    title: String,
    onBackPress: (() -> Unit)? = null,
    options: @Composable (RowScope.() -> Unit)? = null
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 18.dp, bottom = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (onBackPress != null) {
                FlipIconButton(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_back),
                    contentDescription = stringResource(id = R.string.content_desc_arrow_back),
                    onClick = onBackPress,
                    tint = FlipTheme.colors.main
                )
            }
            Text(
                modifier = Modifier
                    .wrapContentSize()
                    .offset(x = (-2).dp),
                text = title,
                style = FlipTheme.typography.headline4,
                textAlign = TextAlign.Center
            )
        }

        if (options != null) {
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                content = options
            )
        }
    }
}

@Preview(name = "Top bar type1(타이틀 왼쪽 정렬) - 뒤로가기 버튼/화면이름", showBackground = true)
@Composable
private fun FlipTopBarPreview() {
    FlipAppTheme {
        FlipTopBar(
            modifier = Modifier.fillMaxWidth(),
            title = "화면 이름",
            onBackPress = { }
        )
    }
}

@Preview(name = "Top bar type1(타이틀 왼쪽 정렬) - 뒤로가기 버튼/화면이름/버튼", showBackground = true)
@Composable
private fun FlipTopBarPreview2() {
    FlipAppTheme {
        FlipTopBar(
            modifier = Modifier.fillMaxWidth(),
            title = "화면 이름",
            onBackPress = { },
            options = {
                FlipTextButton(text = "버튼 이름", onClick = { })
            }
        )
    }
}

@Preview(name = "Top bar type1(타이틀 왼쪽 정렬) - 뒤로가기 버튼/화면이름/더보기 버튼", showBackground = true)
@Composable
private fun FlipTopBarPreview3() {
    FlipAppTheme {
        FlipTopBar(
            modifier = Modifier.fillMaxWidth(),
            title = "화면 이름",
            onBackPress = { },
            options = {
                FlipIconButton(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_more),
                    contentDescription = null,
                    tint = FlipTheme.colors.main,
                    onClick = { }
                )
            }
        )
    }
}