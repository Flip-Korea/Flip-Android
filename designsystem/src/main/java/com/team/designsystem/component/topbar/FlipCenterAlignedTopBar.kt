package com.team.designsystem.component.topbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.team.designsystem.R
import com.team.designsystem.component.button.FlipIconButton
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme

enum class FlipCenterAlignedTopBarActions {
    BACK_PRESS, CLOSE
}

/**
 * 타이틀이 중앙 정렬된 Flip TopBar
 *
 * @param title TopBar 를 사용하는 화면을 설명할 수 있는 제목
 * @param actions 액션 스타일 ([FlipCenterAlignedTopBarActions])
 * @param onAction 액션 (버튼) 클릭 시
 * @param options 오른쪽 부분에 옵션(Composable)을 추가할 수 있다 (RowScope 범위 내)
 */
@Composable
fun FlipCenterAlignedTopBar(
    modifier: Modifier = Modifier,
    title: String,
    actions: FlipCenterAlignedTopBarActions = FlipCenterAlignedTopBarActions.BACK_PRESS,
    onAction: (() -> Unit)? = null,
    options: @Composable (RowScope.() -> Unit)? = null
) {

    Box(modifier = modifier) {
        if (onAction != null) {
            val iconSize = when (actions) {
                FlipCenterAlignedTopBarActions.BACK_PRESS -> { DpSize(24.dp, 24.dp) }
                FlipCenterAlignedTopBarActions.CLOSE -> { DpSize(20.dp, 24.dp) }
            }
            val contendDesc = when(actions) {
                FlipCenterAlignedTopBarActions.BACK_PRESS -> R.string.content_desc_arrow_back
                FlipCenterAlignedTopBarActions.CLOSE -> R.string.content_desc_close
            }
            val imageVector = when(actions) {
                FlipCenterAlignedTopBarActions.BACK_PRESS -> R.drawable.ic_arrow_back
                FlipCenterAlignedTopBarActions.CLOSE -> R.drawable.ic_close
            }
            FlipIconButton(
                modifier = Modifier.align(Alignment.CenterStart),
                imageVector = ImageVector.vectorResource(imageVector),
                contentDescription = stringResource(id = contendDesc),
                iconSize = iconSize,
                onClick = onAction,
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
private fun Preview() {
    FlipAppTheme {
        FlipCenterAlignedTopBar(modifier = Modifier.fillMaxWidth(), title = "화면 이름", onAction = { })
    }
}

@Preview(name = "Top bar type2(타이틀 중앙 정렬) - 화면이름/아이콘 버튼", showBackground = true)
@Composable
private fun Preview2() {
    FlipAppTheme {
        FlipCenterAlignedTopBar(
            modifier = Modifier.fillMaxWidth(),
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
private fun Preview3() {
    FlipAppTheme {
        FlipCenterAlignedTopBar(
            modifier = Modifier.fillMaxWidth(),
            title = "화면 이름",
            onAction = { },
            options = {
                Text(
                    text = "버튼 이름",
                    style = FlipTheme.typography.body6,
                    color = FlipTheme.colors.gray6
                )
            }
        )
    }
}

@Preview(name = "Top bar type(타이틀 중앙 정렬) - 화면이름/닫기 버튼", showBackground = true)
@Composable
private fun Preview4() {
    FlipAppTheme {
        FlipCenterAlignedTopBar(
            modifier = Modifier.fillMaxWidth(),
            actions = FlipCenterAlignedTopBarActions.CLOSE,
            onAction = { },
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