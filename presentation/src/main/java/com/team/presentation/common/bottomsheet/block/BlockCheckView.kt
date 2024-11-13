package com.team.presentation.common.bottomsheet.block

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.team.designsystem.component.button.FlipMediumButton
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme
import com.team.presentation.R

/**
 * Block View 중에서 차단하는 부분
 *
 * @param blockedProfileId 차단할 프로필 ID
 * @param photoUrl 차단할 프로필 사진 Url
 * @param blockState BlockState
 */
@Composable
fun BlockCheckView(
    modifier: Modifier = Modifier,
    blockedProfileId: String,
    photoUrl: String,
    blockState: BlockState,
    onBlockClick: () -> Unit
) {

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AsyncImage(
            modifier = Modifier
                .clip(CircleShape)
                .size(87.dp)
                .padding(bottom = 4.dp),
            model = photoUrl,
            contentDescription = stringResource(id = R.string.home_flip_card_content_desc_photo_url),
            contentScale = ContentScale.Crop
        )
        Text(
            text = "@" + blockedProfileId + stringResource(id = R.string.bottom_sheet_block_title),
            style = FlipTheme.typography.headline3,
            color = FlipTheme.colors.statusRed,
            textAlign = TextAlign.Center
        )
        Text(
            text = buildAnnotatedString {
                append(stringResource(id = R.string.bottom_sheet_block_sub_title_1))
                append(blockedProfileId)
                append(stringResource(id = R.string.bottom_sheet_block_sub_title_2))
                append(blockedProfileId)
                append(stringResource(id = R.string.bottom_sheet_block_sub_title_3))
            },
            style = FlipTheme.typography.body5,
            color = FlipTheme.colors.gray6,
            textAlign = TextAlign.Center
        )

        FlipMediumButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 66.dp),
            text = stringResource(id = R.string.bottom_sheet_block_btn),
            isLoading = blockState.loading,
            onClick = onBlockClick
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BlockCheckViewPreview() {
    FlipAppTheme {
        BlockCheckView(
            blockedProfileId = "profileId",
            photoUrl = "",
            blockState = BlockState(),
            onBlockClick = { }
        )
    }
}