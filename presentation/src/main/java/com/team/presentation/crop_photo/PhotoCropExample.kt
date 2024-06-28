package com.team.presentation.crop_photo

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.designsystem.theme.FlipAppTheme

@Preview
@Composable
fun PhotoCropExample() {

    FlipAppTheme {
        val context = LocalContext.current

        var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
        val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri ->
                if (uri != null) {
                    selectedImageUri = uri
                }
            }
        )

        LaunchedEffect(Unit) {
            singlePhotoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }

        var croppedImage: ImageBitmap? by remember { mutableStateOf(null) }

        Box(modifier = Modifier.fillMaxSize()) {
            if (croppedImage ==  null) {
                PhotoCropScreen(
                    selectedImageUri = selectedImageUri,
                    onCancel = { showToast(context, "취소") },
                    onCrop = {
                        croppedImage = it
                        showToast(context, "성공")
                    }
                )

                /** 사진 선택을 위한 임시 버튼 **/
                PhotoCropButton(
                    modifier = Modifier.fillMaxWidth()
                        .statusBarsPadding()
                        .padding(16.dp),
                    text = "사진 선택",
                    solid = true,
                    onClick = {
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                )
            } else {
                /** 편집한 사진을 보기 위한 임시 화면 **/
                Image(
                    bitmap = croppedImage!!,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )

                Button(modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 30.dp), onClick = { croppedImage = null }) {
                    Text(text = "Retry")
                }
            }
        }
    }
}

private fun showToast(
    context: Context,
    text: String
) {
    Toast.makeText(
        context,
        text,
        Toast.LENGTH_SHORT
    ).show()
}