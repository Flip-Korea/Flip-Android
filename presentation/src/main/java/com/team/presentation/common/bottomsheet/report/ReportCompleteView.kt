package com.team.presentation.common.bottomsheet.report

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.designsystem.component.button.FlipSmallButton
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme
import com.team.presentation.R

/**
 * Report View 중에서 신고 후 완료 부분
 *
 * @param reportedProfileId 신고한 프로필 ID
 * @param onOkClick '괜찮아요' 버튼 클릭 시
 * @param onBlockClick '함께 차단하기' 버튼 클릭 시
 */
@Composable
internal fun ReportCompleteView(
    modifier: Modifier = Modifier,
    reportedProfileId: String,
    onOkClick: () -> Unit,
    onBlockClick: () -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(66.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                modifier = Modifier
                    .size(57.dp)
                    .padding(bottom = 2.dp),
                imageVector = ImageVector.vectorResource(R.drawable.ic_outlined_check),
                contentDescription = stringResource(id = R.string.bottom_sheet_report_content_desc_complete),
                tint = FlipTheme.colors.point
            )
            Text(
                text = stringResource(id = R.string.bottom_sheet_report_complete_title),
                style = FlipTheme.typography.headline6,
                textAlign = TextAlign.Center
            )
            Text(
                text = buildAnnotatedString {
                    append(stringResource(id = R.string.bottom_sheet_report_complete_sub_title_1))
                    append(reportedProfileId)
                    append(stringResource(id = R.string.bottom_sheet_report_complete_sub_title_2))
                },
                style = FlipTheme.typography.body5,
                textAlign = TextAlign.Center
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            FlipSmallButton(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.bottom_sheet_report_complete_btn_ok),
                solid = false,
                onClick = onOkClick
            )
            FlipSmallButton(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.bottom_sheet_report_complete_btn_with_block),
                solid = true,
                onClick = onBlockClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ReportCompleteViewPreview() {
    FlipAppTheme {
        ReportCompleteView(
            reportedProfileId = "profileId",
            onOkClick = { },
            onBlockClick = { }
        )
    }
}