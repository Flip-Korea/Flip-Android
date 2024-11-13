package com.team.presentation.common.bottomsheet.report

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.team.designsystem.theme.FlipAppTheme
import com.team.domain.type.ReportType
import kotlinx.coroutines.launch

private const val PAGE_SIZE = 2

/**
 * 신고 기능을 포함한 Pager 뷰
 *
 * @param reportState ReportState
 * @param reportedProfileId 신고할 프로필 ID
 * @param onReport ReportType 을 기반으로 신고
 * @param onOkClick '괜찮아요' 버튼 클릭 시
 * @param onBlockClick '함께 차단하기' 버튼 클릭 시
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReportPagerView(
    modifier: Modifier = Modifier,
    reportState: ReportState,
    reportedProfileId: String,
    onReport: (ReportType) -> Unit,
    onOkClick: () -> Unit,
    onBlockClick: () -> Unit
) {

    var checkedReportReason: ReportType? by remember { mutableStateOf(null) }

    val pagerState = rememberPagerState { PAGE_SIZE }
    val scope = rememberCoroutineScope()

    LaunchedEffect(reportState) {
        if (reportState.success) {
            pagerState.animateScrollToPage(1)
        }
    }

    HorizontalPager(
        modifier = modifier.fillMaxWidth(),
        state = pagerState,
        userScrollEnabled = false
    ) { page ->
        when (page) {
            0 -> {
                ReportCheckView(
                    reportState = reportState,
                    checkedReportReason = checkedReportReason,
                    onCheck = { reportType ->  checkedReportReason = reportType },
                    onReport = { reportType ->
                        onReport(reportType)
                        //TODO 임시코드, 나중에 reportState 값 결과에 맞게 해줘야 함
                        scope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    }
                )
            }
            1 -> {
                ReportCompleteView(
                    reportedProfileId = reportedProfileId,
                    onOkClick = onOkClick,
                    onBlockClick = onBlockClick
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ReportPagerViewPreview() {
    FlipAppTheme {
        ReportPagerView(
            reportState = ReportState(),
            reportedProfileId = "profileId",
            onReport = { },
            onOkClick = { },
            onBlockClick = { }
        )
    }
}