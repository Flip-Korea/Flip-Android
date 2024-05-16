package com.team.designsystem.component.button

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.designsystem.component.utils.clickableSingle
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme

@Composable
fun FlipTextButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
) {
    Text(
        modifier = modifier
            .wrapContentSize()
            .clickableSingle { onClick() },
        text = text,
        style = FlipTheme.typography.body4Underline,
        color = FlipTheme.colors.gray5
    )
}

@Preview(showBackground = true, heightDp = 200)
@Composable
private fun FlipTextButtonPreview() {
    FlipAppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            FlipTextButton(
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 16.dp),
                text = "sample",
                onClick = {}
            )
        }
    }
}