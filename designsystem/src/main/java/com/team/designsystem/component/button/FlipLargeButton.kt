package com.team.designsystem.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.designsystem.component.utils.ClickableSingle
import com.team.designsystem.component.utils.get
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme

@Composable
fun FlipLargeButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {

    val clickableSingle = remember { ClickableSingle.get() }

    Button(
        onClick = { clickableSingle.onEvent(onClick) },
        modifier = modifier
            .fillMaxWidth()
            .height(78.dp)
            .background(if (!enabled) FlipTheme.colors.gray4 else Color.Transparent),
        colors = ButtonDefaults.buttonColors(
            containerColor = FlipTheme.colors.main,
            contentColor = FlipTheme.colors.white
        ),
        enabled = enabled,
        shape = RectangleShape,
        contentPadding = PaddingValues(top = 15.dp, bottom = 15.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxHeight(),
            text = text,
            style = FlipTheme.typography.headline3,
            color = FlipTheme.colors.white
        )
    }
}

@Preview(name = "enabled", showBackground = true)
@Composable
private fun FlipLargeButtonPreview() {
    FlipAppTheme {
        FlipLargeButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            text = "sample",
            enabled = true,
            onClick = {}
        )
    }
}

@Preview(name = "disabled", showBackground = true)
@Composable
private fun FlipLargeButton2Preview() {
    FlipAppTheme {
        FlipLargeButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            text = "sample",
            enabled = false,
            onClick = {}
        )
    }
}