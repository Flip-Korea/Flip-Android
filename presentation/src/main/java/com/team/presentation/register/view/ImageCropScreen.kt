package com.team.presentation.register.view

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import com.team.designsystem.theme.FlipAppTheme
import com.team.designsystem.theme.FlipTheme
import com.team.presentation.photocrop.PhotoCropper

@Composable
fun ImageCropScreen(
    modifier: Modifier = Modifier,
    onCancel: () -> Unit,
    onCrop: (ImageBitmap) -> Unit,
) {
    var selectedImageUri: Uri? by rememberSaveable { mutableStateOf(null) }
    val singlePhotoPickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri ->
                if (uri != null) {
                    selectedImageUri = uri
                }
            },
        )
    var photoCropperVisible by rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        singlePhotoPickerLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
        )
    }

    Box(modifier = modifier.background(FlipTheme.colors.main)) {
        if (photoCropperVisible) {
            PhotoCropper(
                selectedImageUri = selectedImageUri,
                onCancel = {
                    photoCropperVisible = false
                    onCancel()
                },
                onCrop = { imageBitmap ->
                    photoCropperVisible = false
                    onCrop(imageBitmap)
                },
            )
        }
    }
}

@Preview
@Composable
private fun ImageCropScreenPreview() {
    FlipAppTheme {
        ImageCropScreen(
            onCancel = {},
            onCrop = {},
        )
    }
}
