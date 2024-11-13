package com.team.presentation.common.bottomsheet.report

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.designsystem.component.button.FlipMediumButton
import com.team.designsystem.component.dropdown.FlipRadioButton
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme
import com.team.domain.type.ReportType
import com.team.presentation.R

/**
 * Report View 중에서 신고 사유를 체크하는 부분
 *
 * @param reportState ReportState
 * @param checkedReportReason 신고 사유(ReportType)
 * @param onCheck 신고 사유 체크 시
 * @param onReport '신고하기' 버튼 클릭 시
 * @see ReportType
 */
@Composable
internal fun ReportCheckView(
    modifier: Modifier = Modifier,
    reportState: ReportState,
    checkedReportReason: ReportType?,
    onCheck: (ReportType?) -> Unit,
    onReport: (ReportType) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        TopSection(
            title = stringResource(id = R.string.bottom_sheet_report_title),
            subTitle = stringResource(id = R.string.bottom_sheet_report_sub_title)
        )
        MiddleSection(
            reportReasons = reportReasons,
            onCheck = { reportType -> onCheck(reportType) }
        )
        FlipMediumButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            enabled = checkedReportReason != null,
            isLoading = reportState.loading,
            text = stringResource(id = R.string.bottom_sheet_report_btn),
            onClick = { checkedReportReason?.let { onReport(it) } }
        )
    }
}

@Composable
private fun TopSection(
    modifier: Modifier = Modifier,
    title: String,
    subTitle: String
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 26.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = title,
            style = FlipTheme.typography.headline5,
            color = FlipTheme.colors.statusRed,
            textAlign = TextAlign.Center
        )
        Text(
            text = subTitle,
            style = FlipTheme.typography.body5,
            color = FlipTheme.colors.gray6,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun MiddleSection(
    modifier: Modifier = Modifier,
    reportReasons: List<ReportType>,
    onCheck: (ReportType?) -> Unit,
) {

    var checkedReason: ReportType? by remember { mutableStateOf(null) }

    Column(modifier = modifier.fillMaxWidth()) {
        reportReasons.forEach { reason ->
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = FlipTheme.colors.gray2
            )
            Row(
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            checkedReason = reason
                            onCheck(checkedReason)
                        }
                    )
                    .padding(horizontal = 16.dp, vertical = 13.5.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp, alignment = Alignment.Start),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FlipRadioButton(
                    checked = reason == checkedReason,
                    onCheckedChange = {
                        checkedReason = reason
                        onCheck(checkedReason)
                    }
                )
                Text(
                    text = reason.asString(),
                    style = FlipTheme.typography.body5,
                    textAlign = TextAlign.Start
                )
            }
            if (reason == ReportType.HateSpeech) {
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = FlipTheme.colors.gray2
                )
            }
        }
    }
}

private val reportReasons = listOf(
    ReportType.DontLike,
    ReportType.SpamAndAdvertising,
    ReportType.Inappropriate,
    ReportType.Fake,
    ReportType.HateSpeech,
)

@Preview
@Composable
private fun TopSectionPreview() {
    FlipAppTheme {
        TopSection(
            title = "게시글을 신고하시겠어요?",
            subTitle = "이 게시글을 신고합니다. 신고 내용은 Flip 이용약관 및" +
                    "정책에 의해서 처리되며, 허위신고 시 서비스 이용이" +
                    "제한될 수 있습니다."
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MiddleSectionPreview() {
    FlipAppTheme {
        MiddleSection(
            reportReasons = reportReasons,
            onCheck = {}
        )
    }
}