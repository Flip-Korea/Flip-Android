package com.team.presentation.common.bottomsheet.block

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.designsystem.component.button.FlipMediumButton
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme
import com.team.presentation.R

/**
 * Block View 중에서 차단 후 완료 부분
 */
@Composable
fun BlockCompleteView(
    modifier: Modifier = Modifier,
    blockedProfileId: String,
    onOkClick: () -> Unit
) {

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            modifier = Modifier.size(57.dp).padding(bottom = 2.dp),
            imageVector = ImageVector.vectorResource(R.drawable.ic_outlined_check),
            contentDescription = stringResource(id = R.string.bottom_sheet_block_content_desc_complete),
            tint = FlipTheme.colors.point
        )
        Text(
            text = stringResource(id = R.string.bottom_sheet_block_complete_title),
            style = FlipTheme.typography.headline5,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(id = R.string.bottom_sheet_block_complete_sub_title_1) +
                    blockedProfileId +
                    stringResource(id = R.string.bottom_sheet_block_complete_sub_title_2),
            style = FlipTheme.typography.body5,
            textAlign = TextAlign.Center,
            color = FlipTheme.colors.gray6
        )

        FlipMediumButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 66.dp),
            text = stringResource(id = R.string.bottom_sheet_block_btn_ok),
            onClick = onOkClick
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BlockCompleteViewPreview() {
    FlipAppTheme {
        BlockCompleteView(
            blockedProfileId = "profileId",
            onOkClick = { }
        )
    }
}