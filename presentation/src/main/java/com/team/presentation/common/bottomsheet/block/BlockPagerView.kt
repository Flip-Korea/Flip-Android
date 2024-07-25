package com.team.presentation.common.bottomsheet.block

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.team.designsystem.theme.FlipAppTheme
import kotlinx.coroutines.launch

private const val PAGE_SIZE = 2

/**
 * 차단 기능을 포함한 Pager 뷰
 *
 * @param blockedProfileId 차단할 프로필 ID
 * @param photoUrl 차단할 프로필 Url
 * @param blockState BlockState
 * @param onBlockClick '차단하기' 버튼 클릭 시
 * @param onOkClick '확인' 버튼 클릭 시
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BlockPagerView(
    modifier: Modifier = Modifier,
    blockedProfileId: String,
    photoUrl: String,
    blockState: BlockState,
    onBlockClick: () -> Unit,
    onOkClick: () -> Unit
) {

    val pagerState = rememberPagerState { PAGE_SIZE }
    val scope = rememberCoroutineScope()

    LaunchedEffect(blockState) {
        if (blockState.success) {
            pagerState.animateScrollToPage(1)
        }
    }

    HorizontalPager(
        modifier = modifier.fillMaxWidth(),
        state = pagerState,
        userScrollEnabled = false
    ) { page ->
        when(page) {
            0 -> {
                BlockCheckView(
                    blockedProfileId = blockedProfileId,
                    photoUrl = photoUrl,
                    blockState = blockState,
                    onBlockClick = {
                        onBlockClick()
                        //TODO 임시코드, 나중에 blockState 값 결과에 맞게 해줘야 함
                        scope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    }
                )
            }

            1 -> {
                BlockCompleteView(
                    blockedProfileId = blockedProfileId,
                    onOkClick = onOkClick
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BlockPagerViewPreview() {
    FlipAppTheme {
        BlockPagerView(
            blockedProfileId = "profileId",
            photoUrl = "",
            blockState = BlockState(),
            onBlockClick = { },
            onOkClick = { }
        )
    }
}