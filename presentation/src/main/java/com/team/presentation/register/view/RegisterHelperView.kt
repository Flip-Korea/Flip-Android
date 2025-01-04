package com.team.presentation.register.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme
import com.team.presentation.R

@Composable
fun RegisterProgressView(
    modifier: Modifier = Modifier,
    currentStep: Int,
    totalSteps: Int,
) {
    Text(
        modifier = modifier,
        text =
            buildAnnotatedString {
                withStyle(SpanStyle(color = FlipTheme.colors.point)) {
                    append("$currentStep")
                }
                append(SLASH)
                append("$totalSteps")
            },
        style = FlipTheme.typography.body7,
    )
}

@Composable
fun RegisterTitleView(
    modifier: Modifier = Modifier,
    mainTitle: AnnotatedString,
    subTitle: String? = null,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = mainTitle,
            style = FlipTheme.typography.headline7,
        )
        subTitle?.let {
            Text(
                text = subTitle,
                style = FlipTheme.typography.body3,
            )
        }
    }
}

private const val SLASH = "/"

@Preview(showBackground = true)
@Composable
private fun RegisterProgressViewPreview() {
    FlipAppTheme {
        RegisterProgressView(currentStep = 1, totalSteps = 3)
    }
}

@Preview(showBackground = true)
@Composable
private fun RegisterTitleViewPreview() {
    FlipAppTheme {
        RegisterTitleView(
            mainTitle = buildAnnotatedString {
                append(stringResource(id = R.string.terms_of_service_screen_input_name_title_1))
                append("\n")
                withStyle(FlipTheme.typography.headline8.toSpanStyle()) {
                    append(stringResource(id = R.string.terms_of_service_screen_input_name_title_2))
                }
                append(stringResource(id = R.string.terms_of_service_screen_input_name_title_3))
            },
            subTitle = stringResource(id = R.string.terms_of_service_screen_input_name_sub_title),
        )
    }
}
