package com.team.presentation.register.view

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.team.designsystem.component.utils.clickableSingle
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme
import com.team.presentation.R
import com.team.presentation.register.state.InputImageState

@Composable
fun InputImageScreen(
    modifier: Modifier = Modifier,
    currentStep: Int,
    totalSteps: Int,
    inputImageState: InputImageState,
    openPhotoCropper: () -> Unit,
) {
    Column(
        modifier = modifier.zIndex(1f),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        RegisterProgressView(
            modifier = Modifier.fillMaxWidth(),
            currentStep = currentStep,
            totalSteps = totalSteps,
        )
        RegisterTitleView(
            mainTitle =
                buildAnnotatedString {
                    append(
                        stringResource(id = R.string.register_screen_input_photo_title_1),
                    )
                    append("\n")
                    withStyle(FlipTheme.typography.headline8.toSpanStyle()) {
                        append(
                            stringResource(
                                id = R.string.register_screen_input_photo_title_2,
                            ),
                        )
                    }
                    append(
                        stringResource(id = R.string.register_screen_input_photo_title_3),
                    )
                },
        )
        Spacer(modifier = Modifier.height(94.dp))
        Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            if (inputImageState.imageBitmap != null) {
                SelectedImage(
                    modifier =
                        Modifier
                            .clip(CircleShape)
                            .size(ImageSize)
                            .clickableSingle { openPhotoCropper() },
                    image = inputImageState.imageBitmap,
                )
            } else {
                SelectImageButton(
                    modifier = Modifier.size(ImageSize),
                    image = R.drawable.ic_camera,
                    onClick = { openPhotoCropper() },
                )
            }
        }
    }
}

@Composable
private fun SelectedImage(
    modifier: Modifier = Modifier,
    image: ImageBitmap,
) {
    Image(
        modifier = modifier.clip(CircleShape),
        bitmap = image,
        contentDescription = null,
        alignment = Alignment.Center,
    )
}

@Composable
private fun SelectImageButton(
    modifier: Modifier = Modifier,
    @DrawableRes image: Int,
    onClick: () -> Unit,
) {
    Box(
        modifier =
            modifier
                .clip(CircleShape)
                .background(FlipTheme.colors.gray1, CircleShape)
                .clickableSingle { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            modifier = Modifier.size(54.dp),
            imageVector = ImageVector.vectorResource(id = image),
            contentDescription =
                stringResource(id = R.string.register_screen_content_desc_input_photo_btn),
            tint = FlipTheme.colors.gray4,
        )
    }
}

private val ImageSize = 182.dp

@Preview
@Composable
private fun SelectImageButtonPreview() {
    FlipAppTheme {
        SelectImageButton(
            modifier = Modifier.size(182.dp),
            image = R.drawable.ic_camera,
            onClick = { },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun InputImageScreenPreview() {
    FlipAppTheme {
        InputImageScreen(
            currentStep = 3,
            totalSteps = 3,
            inputImageState = InputImageState(),
            openPhotoCropper = { },
        )
    }
}
