package com.team.designsystem.component.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.designsystem.R
import com.team.designsystem.component.utils.ClickableSingle
import com.team.designsystem.component.utils.get
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme

@Composable
fun FlipEditButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {

    val clickableSingle = remember { ClickableSingle.get() }

    OutlinedButton(
        onClick = { clickableSingle.onEvent(onClick) },
        modifier = modifier
            .fillMaxWidth()
            .height(34.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = FlipTheme.colors.white,
            contentColor = FlipTheme.colors.main
        ),
        shape = FlipTheme.shapes.roundedCornerSmall,
        border = BorderStroke(1.dp, FlipTheme.colors.main),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = stringResource(id = R.string.btn_flip_edit),
            style = FlipTheme.typography.headline1,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FlipEditButtonPreview() {
    FlipAppTheme {
        Box(modifier = Modifier.size(200.dp)) {
            FlipEditButton(modifier = Modifier
                .align(Alignment.Center)
                .width(168.dp), onClick = {})
        }
    }
}