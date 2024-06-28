package com.team.presentation.crop_photo

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.designsystem.component.utils.ClickableSingle
import com.team.designsystem.component.utils.get
import com.team.designsystem.theme.FlipTheme

/**
 * PhotoCropScreen 을 위한 버튼
 *
 * @see PhotoCropScreen
 */
@Composable
fun PhotoCropButton(
    modifier: Modifier = Modifier,
    text: String,
    solid: Boolean,
    onClick: () -> Unit
) {

    val clickableSingle = remember { ClickableSingle.get() }

    OutlinedButton(
        onClick = { clickableSingle.onEvent(onClick) },
        modifier = modifier.height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (solid) FlipTheme.colors.white else Color.Transparent,
            contentColor = if (solid) FlipTheme.colors.main else FlipTheme.colors.white
        ),
        shape = FlipTheme.shapes.roundedCornerSmall,
        contentPadding = PaddingValues(vertical = 12.dp),
        border = BorderStroke(1.dp, if (solid) Color.Transparent else FlipTheme.colors.white)
    ) {
        Text(
            text = text,
            style = FlipTheme.typography.headline3,
            color = if (solid) FlipTheme.colors.main else FlipTheme.colors.white
        )
    }
}

@Preview
@Composable
private fun PhotoCropButtonPreview() {
    Row {
        PhotoCropButton(
            modifier = Modifier.weight(1f),
            text = "취소",
            solid = false,
            onClick = {},
        )
        PhotoCropButton(
            modifier = Modifier.weight(1f),
            text = "확인",
            solid = true,
            onClick = {},
        )
    }
}