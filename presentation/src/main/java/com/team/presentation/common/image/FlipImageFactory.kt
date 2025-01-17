package com.team.presentation.common.image

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import java.io.ByteArrayOutputStream

class FlipImageFactory(
    private val imageBitmap: ImageBitmap,
) {
    fun create(): FlipImage = FlipImage(imageBitmap.toByteArray())

    private fun ImageBitmap.toByteArray(
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
        quality: Int = 100,
    ): ByteArray {
        val bitmap = this.asAndroidBitmap()
        return ByteArrayOutputStream().use { outputStream ->
            bitmap.compress(format, quality, outputStream)
            outputStream.toByteArray()
        }
    }
}
