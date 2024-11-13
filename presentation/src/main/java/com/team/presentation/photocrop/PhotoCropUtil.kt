package com.team.presentation.photocrop

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import kotlin.math.max

/**
 * 잘라낸 이미지 결과
 * 1. Success: 성공 시 imageBitmap 타입의 이미지를 포함
 * 2. Failure: 실패 시 errorMessage 문자열을 포함 (nullable)
 */
sealed interface CropImageResult {
    data class Success(val imageBitmap: ImageBitmap): CropImageResult
    data class Failure(val errorMessage: String? = null): CropImageResult
}

/**
 * 이미지 자르기 함수
 *
 * 주의사항: 사용 시 (시작 오프셋 + (너비 or 높이)) 가 이미지의 크기를 초과하면 안된다.
 *
 * @param density dp 에서 px 로 변환을 위해 사용
 * @param imageBitmap ImageBitmap 타입의 이미지 (nullable)
 * @param scale 이미지의 Scale 값
 * @param viewWidth 화면 사이즈의 가로 길이
 * @param viewHeight 화면 사이즈의 가로 길이
 * @param offsetChanged 자르고 싶은 부분의 Offset 값(기준은 TopLeft)
 *
 * @return CropImageResult
 */
fun cropImage(
    density: Density,
    imageBitmap: ImageBitmap?,
    scale: Float = 1f,
    viewWidth: Int,
    viewHeight: Int,
    offsetChanged: Offset
): CropImageResult {

    return if (imageBitmap == null) {
        CropImageResult.Failure("다른 사진으로 시도 해 주세요.")
    } else {
        val imageWidth = imageBitmap.width
        val imageHeight = imageBitmap.height
        val widthRatio = imageBitmap.width / viewWidth.toFloat()
        val heightRatio = imageBitmap.height / viewHeight.toFloat()

        val maxRatio = max(widthRatio, heightRatio)
        val radiusPx = with(density) { HOLE_RADIUS.dp.toPx() } * maxRatio

        val centerX = imageWidth / 2f
        val centerY = imageHeight / 2f

        val srcOffsetX = (centerX - radiusPx / scale) + (-offsetChanged.x * maxRatio)
        val srcOffsetY = (centerY - radiusPx / scale) + (-offsetChanged.y * maxRatio)

        val width = (radiusPx * 2).toInt()
        val height = (radiusPx * 2).toInt()

        val croppedImage = ImageBitmap(width, height)
        val canvas = Canvas(croppedImage)

        canvas.scale(scale, scale)
        canvas.drawImageRect(
            image = imageBitmap,
            srcOffset = IntOffset(srcOffsetX.toInt(), srcOffsetY.toInt()),
            srcSize = IntSize(width, height),
            dstOffset = IntOffset(0, 0),
            dstSize = IntSize(width, height),
            paint = Paint()
        )
        CropImageResult.Success(croppedImage)
    }
}

/**
 * Uri 를 Bitmap(ImageBitmap)으로 변환 시켜준다.
 * @param imageUri 이미지의 Uri
 *
 * @return ImageBitmap(nullable)
 */
suspend fun uriToBitmap(
    context: Context,
    imageUri: Uri,
): ImageBitmap? {
    var bitmap: Bitmap? = null

    val loader = ImageLoader(context)
    val request = ImageRequest.Builder(context)
        .data(imageUri)
        .allowHardware(false)
        .build()
    val result = loader.execute(request)
    if (result is SuccessResult) {
        bitmap = (result.drawable as BitmapDrawable).bitmap
    }

    return bitmap?.asImageBitmap()
}