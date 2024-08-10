package com.team.designsystem.component.snackbar

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.designsystem.R
import com.team.designsystem.component.utils.clickableSingleWithoutRipple
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Flip 스낵바
 *
 * @param snackBarHostState SnackbarHostState
 * @param iconRes 스낵바 메시지에 표시할 아이콘 리소스
 * @param actionTextRes 오른쪽 영역에 표시할 액션 텍스트 리소스
 * @param onActionClick 액션 버튼 클릭 시
 */
@Composable
fun FlipSnackbar(
    modifier: Modifier = Modifier,
    snackBarHostState: SnackbarHostState,
    @DrawableRes iconRes: Int? = null,
    @StringRes actionTextRes: Int? = null,
    onActionClick: (() -> Unit)? = null,
) {
    SnackbarHost(
        modifier = Modifier.fillMaxWidth(),
        hostState = snackBarHostState,
    ) {
        Snackbar(
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = 61.dp)
                .padding(start = 16.dp, end = 16.dp, bottom = 12.dp),
            containerColor = FlipTheme.colors.main,
            contentColor = FlipTheme.colors.white,
            shape = FlipTheme.shapes.roundedCornerSmall,
            action = {
                if (actionTextRes != null && onActionClick != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.End)
                            .padding(end = 10.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(
                            modifier = Modifier.clickableSingleWithoutRipple { onActionClick() },
                            text = stringResource(id = actionTextRes),
                            style = FlipTheme.typography.headline2,
                            color = FlipTheme.colors.point2
                        )
                    }
                }
            }
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                iconRes?.let { icon ->
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = ImageVector.vectorResource(icon),
                        contentDescription = stringResource(id = R.string.snackbar)
                    )
                }
                Text(
                    modifier = Modifier
                        .weight(3f)
                        .wrapContentWidth(Alignment.Start),
                    text = snackBarHostState.currentSnackbarData?.visuals?.message ?: "",
                    style = FlipTheme.typography.body5,
                    color = FlipTheme.colors.white,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FlipSnackbarPreview() {

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var snackbarJob: Job? by remember { mutableStateOf(null) }

    FlipAppTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = {
                FlipSnackbar(
                    snackBarHostState = snackbarHostState,
                    iconRes = R.drawable.ic_outlined_setting,
                    actionTextRes = R.string.btn_follow_back,
                    onActionClick = {
                        snackbarJob?.cancel()
                    }
                )
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                Button(
                    modifier = Modifier.align(Alignment.Center),
                    onClick = {
                        snackbarJob?.cancel()
                        snackbarJob = coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                message = "네트워크 상태를 확인 해 주세요."
                            )
                        }
                    }
                ) {
                    Text(text = "Show Snackbar")
                }

                Text(modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp), text = "asdasdljalskdjlaksdjlaks")
            }
        }
    }
}