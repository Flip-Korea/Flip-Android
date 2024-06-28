package com.team.presentation.home.view

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.designsystem.component.button.FlipIconButton
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme
import com.team.presentation.R

/**
 * 홈 화면에서 사용되는 TopBar
 * @param icons 옵션 아이콘들
 */
@Composable
fun HomeTopBar(
    modifier: Modifier = Modifier,
    @DrawableRes logo: Int,
    onSearchClick: () -> Unit,
    onNotiClick: () -> Unit,
    onSettingClick: () -> Unit
) {

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.size(48.dp, 30.dp),
            imageVector = ImageVector.vectorResource(logo),
            contentDescription = stringResource(id = R.string.home_screen_content_desc_logo)
        )
        Row {
            FlipIconButton(
                imageVector = ImageVector.vectorResource(R.drawable.ic_outlined_search),
                contentDescription = stringResource(id = R.string.home_screen_content_desc_search),
                tint = FlipTheme.colors.main,
                onClick = onSearchClick
            )
            FlipIconButton(
                imageVector = ImageVector.vectorResource(R.drawable.ic_outlined_notification),
                contentDescription = stringResource(id = R.string.home_screen_content_desc_notification),
                tint = FlipTheme.colors.main,
                onClick = onNotiClick
            )
            FlipIconButton(
                imageVector = ImageVector.vectorResource(R.drawable.ic_outlined_setting),
                contentDescription = stringResource(id = R.string.home_screen_content_desc_setting),
                tint = FlipTheme.colors.main,
                onClick = onSettingClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeTopBarPreview() {
    FlipAppTheme {
        HomeTopBar(
            modifier = Modifier.fillMaxWidth(),
            logo = R.drawable.ic_logo_dark,
            onSearchClick = { },
            onNotiClick = { },
            onSettingClick = { }
        )
    }
}