package com.team.designsystem.component.topbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
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
 * 타이틀이 왼쪽 정렬된 Flip TopBar
 *
 * @param title TopBar 를 사용하는 화면을 설명할 수 있는 제목
 * @param onBackPress 뒤로가기 버튼 클릭 시
 * @param options 오른쪽 부분에 옵션(Composable)을 추가할 수 있다 (RowScope 범위 내)
 */
@Composable
fun FlipTopBar(
    modifier: Modifier = Modifier,
    title: String,
    onBackPress: (() -> Unit)? = null,
    options: @Composable (RowScope.() -> Unit)? = null
) {

    Row(
        modifier = modifier,
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