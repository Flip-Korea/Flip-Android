package com.team.designsystem.component.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.designsystem.component.utils.ClickableSingle
import com.team.designsystem.component.utils.get
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme
import kotlinx.coroutines.delay

@Composable
fun FlipSmallButton(
    modifier: Modifier = Modifier,
    text: String,
    solid: Boolean,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    onClick: () -> Unit,
) {

    val clickableSingle = remember { ClickableSingle.get() }

    Button(
        onClick = { if (!isLoading) clickableSingle.onEvent(onClick) },
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (solid) FlipTheme.colors.main else FlipTheme.colors.gray2,
            contentColor = if (solid) FlipTheme.colors.white else FlipTheme.colors.gray7
        ),
        enabled = enabled,
        shape = FlipTheme.shapes.roundedCornerSmall,
        contentPadding = PaddingValues(0.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(34.dp),
                color = if (solid) FlipTheme.colors.white else FlipTheme.colors.main,
                strokeWidth = 3.dp,
            )
        } else {
            Text(
                text = text,
                style = FlipTheme.typography.headline3,
                color = if (solid) {
                    FlipTheme.colors.white
                } else FlipTheme.colors.gray7
            )
        }
    }
}

@Preview(name = "solid & tinted", showBackground = true, backgroundColor = 0xFF808080)
@Composable
private fun FlipSmallButtonPreview() {
    FlipAppTheme {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FlipSmallButton(
                modifier = Modifier.weight(1f),
                text = "sample",
                solid = true,
                onClick = {}
            )
            FlipSmallButton(
                modifier = Modifier.weight(1f),
                text = "sample",
                solid = false,
                onClick = {}
            )
        }
    }
}

@Preview(name = "loading", showBackground = true, backgroundColor = 0xFF808080)
@Composable
private fun FlipSmallButton2Preview() {

    var isClick by remember { mutableStateOf(false) }
    var isClick2 by remember { mutableStateOf(false) }

    LaunchedEffect(isClick, isClick2) {
        if (isClick) {
            delay(2000L)
            isClick = false
        }
        if (isClick2) {
            delay(2000L)
            isClick2 = false
        }
    }

    FlipAppTheme {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FlipSmallButton(
                modifier = Modifier.weight(1f),
                text = "sample",
                solid = true,
                isLoading = isClick,
                onClick = { isClick = !isClick }
            )
            FlipSmallButton(
                modifier = Modifier.weight(1f),
                text = "sample",
                solid = false,
                isLoading = isClick2,
                onClick = { isClick2 = !isClick2 }
            )
        }
    }
}