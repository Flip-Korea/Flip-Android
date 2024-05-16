package com.team.designsystem.component.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
fun FlipProfileEditButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {

    val clickableSingle = remember { ClickableSingle.get() }

    Button(
        onClick = { clickableSingle.onEvent(onClick) },
        modifier = modifier
            .fillMaxWidth()
            .height(34.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = FlipTheme.colors.main,
            contentColor = FlipTheme.colors.white
        ),
        shape = FlipTheme.shapes.roundedCornerSmall,
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = stringResource(id = R.string.btn_profile_edit),
            style = FlipTheme.typography.headline1,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FlipProfileEditButtonPreview() {
    FlipAppTheme {
        FlipProfileEditButton(onClick = {})
    }
}