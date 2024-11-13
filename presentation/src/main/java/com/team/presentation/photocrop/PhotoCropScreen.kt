package com.team.presentation.photocrop

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.team.presentation.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

/**
 * 이미지 자르기 화면
 *
 * @param selectedImageUri 자를 이미지 Uri
 * @param onCancel 취소 버튼 클릭 시
 * @param onCrop 확인 버튼 클릭 시 이미지를 자르고 자른 이미지를 제공
 *
 * @see CropImageResult
 * @see cropImage
 * @see uriToBitmap
 */
@Composable
fun PhotoCropScreen(
    modifier: Modifier = Modifier,
    selectedImageUri: Uri?,
    onCancel: () -> Unit,
    onCrop: (ImageBitmap) -> Unit
) {

    val density = LocalDensity.current
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var viewWidth by remember { mutableIntStateOf(0) }
    var viewHeight by remember { mutableIntStateOf(0) }

    var imageBitmap: ImageBitmap? by remember { mutableStateOf(null) }

    var scale by remember { mutableFloatStateOf(1.5f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val imageTransformState =
        rememberTransformableState { zoomChange, offsetChange, rotationChange ->
            val maxX = (viewWidth.toFloat() * (scale - 1f) / 2f) / scale
            val maxY = (viewHeight.toFloat() * (scale - 1f) / 2f) / scale
            scale = (scale * zoomChange).coerceIn(1f, 3f)
            offset = Offset(
                x = (offset.x + offsetChange.x).coerceIn(-maxX, maxX),
                y = (offset.y + offsetChange.y).coerceIn(-maxY, maxY)
            )
        }

    LaunchedEffect(selectedImageUri) {
        selectedImageUri?.let { image ->
            val imageBitmapDeferred = coroutineScope.async(Dispatchers.IO) {
                uriToBitmap(context, image)
            }
            imageBitmap = imageBitmapDeferred.await()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .onSizeChanged {
                viewWidth = it.width
                viewHeight = it.height
            }
    ) {
        HoleFrameView {
            imageBitmap?.let { imageBitmap ->
                Image(
                    bitmap = imageBitmap,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .align(Alignment.Center)
//                        .clipToBounds()
                        .graphicsLayer(
                            scaleX = scale,
                            scaleY = scale,
                            translationX = offset.x * scale,
                            translationY = offset.y * scale,
                        )
                        .transformable(imageTransformState)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(start = 16.dp, end = 16.dp, bottom = 47.dp),
            horizontalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            PhotoCropButton(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.photo_crop_screen_btn_cancel),
                solid = false,
                onClick = onCancel
            )
            PhotoCropButton(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.photo_crop_screen_btn_ok),
                solid = true,
                onClick = {
                    imageBitmap?.let { imageBitmap ->
                        val cropImageResult = cropImage(
                            density = density,
                            imageBitmap = imageBitmap,
                            scale = scale,
                            viewWidth = viewWidth,
                            viewHeight = viewHeight,
                            offsetChanged = offset
                        )
                        when (cropImageResult) {
                            is CropImageResult.Success -> {
                                onCrop(cropImageResult.imageBitmap)
                            }
                            is CropImageResult.Failure -> { /** 에러 처리 **/ }
                        }
                    }
                }
            )
        }
    }
}

/**
 * 이미지를 자를 부분을 선택하는 원 모양의 구멍이 뚫린 프레임 뷰
 */
@Composable
private fun HoleFrameView(
    modifier: Modifier = Modifier,
    image: @Composable () -> Unit,
) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .drawWithContent {
                /** px **/
                val width = size.width
                /** px **/
                val height = size.height

                drawContent()

                drawWithLayer {
                    drawRect(Color(0xFF212121).copy(0.8f))

                    drawCircle(
                        color = Color.Transparent,
//                        radius = (width / 2) - padding,
                        radius = HOLE_RADIUS.dp.toPx(),
                        /** px (171dp 에서 변환된 값) **/
                        center = Offset(width / 2, height / 2),
                        blendMode = BlendMode.SrcIn
                    )
                }
            }
    ) {
        image()
    }
}

/**
 * BlendMode 와 함께 레이어를 그려 겹치게 해준다.
 */
private fun DrawScope.drawWithLayer(block: DrawScope.() -> Unit) {
    with(drawContext.canvas.nativeCanvas) {
        val checkPoint = saveLayer(null, null)
        block()
        restoreToCount(checkPoint)
    }
}

/** px: 480.9375 */
const val HOLE_RADIUS = 171