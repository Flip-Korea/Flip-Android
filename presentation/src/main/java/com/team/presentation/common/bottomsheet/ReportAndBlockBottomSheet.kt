package com.team.presentation.common.bottomsheet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.team.designsystem.theme.FlipAppTheme
import com.team.domain.type.ReportType
import com.team.presentation.common.bottomsheet.block.BlockPagerView
import com.team.presentation.common.bottomsheet.block.BlockState
import com.team.presentation.common.bottomsheet.report.ReportPagerView
import com.team.presentation.common.bottomsheet.report.ReportState
import kotlinx.coroutines.launch

/**
 * 신고 & 차단 기능이 있는 Bottom Sheet
 *
 * @param startFromReportView BottomSheet 의 첫 화면이 신고 화면 인지에 대한 여부
 * @param sheetState Bottom Sheet State
 * @param reportState ReportState
 * @param blockState BlockState
 * @param profileId 신고 / 차단 할 프로필 ID
 * @param photoUrl 신고 / 차단 할 프로필 사진 Url
 * @param onDismissRequest Bottom Sheet 사라질 때 요청
 * @param onReport 신고 기능 수행
 * @param onBlockClick 차단 기능 수행
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportAndBlockBottomSheet(
    modifier: Modifier = Modifier,
    startFromReportView: Boolean?,
    sheetState: SheetState,
    reportState: ReportState,
    blockState: BlockState,
    profileId: String,
    photoUrl: String,
    onDismissRequest: () -> Unit,
    onReport: (ReportType) -> Unit,
    onBlockClick: () -> Unit,
) {

    startFromReportView?.let {

        var isReportView by remember(startFromReportView) { mutableStateOf(startFromReportView) }

        FlipModalBottomSheet(
            modifier = modifier.fillMaxWidth(),
            onDismissRequest = onDismissRequest,
            sheetState = sheetState,
        ) { bottomSheetModifier ->
            //TODO Bottom 패딩 기준, 화면 바닥부터 인지 시스템 바부터 인지 확인해 보기
            Box(modifier = bottomSheetModifier.padding(bottom = BottomSheetTokens.bottomPadding)) {
                if (startFromReportView && isReportView) {
                    ReportPagerView(
                        reportState = reportState,
                        reportedProfileId = profileId,
                        onReport = { onReport(it) },
                        onOkClick = onDismissRequest,
                        onBlockClick = { isReportView = false }
                    )
                } else {
                    BlockPagerView(
                        blockedProfileId = profileId,
                        photoUrl = photoUrl,
                        blockState = blockState,
                        onBlockClick = onBlockClick,
                        onOkClick = onDismissRequest
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
private fun ReportAndBlockBottomSheetPreview() {

    FlipAppTheme {

        var showBottomSheet by remember { mutableStateOf(false) }
        val coroutineScope = rememberCoroutineScope()
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

        if (showBottomSheet) {
            ReportAndBlockBottomSheet(
                modifier = Modifier,
                startFromReportView = false,
                sheetState = sheetState,
                reportState = ReportState(),
                blockState = BlockState(),
                profileId = "",
                photoUrl = "",
                onDismissRequest = {
                    coroutineScope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showBottomSheet = false
                        }
                    }
                },
                onReport = {},
                onBlockClick = {},
            )
        }

        Box(modifier = Modifier.fillMaxSize()) {
            Button(onClick = { showBottomSheet = true }) {
                Text(text = "Open")
            }
        }
    }
}