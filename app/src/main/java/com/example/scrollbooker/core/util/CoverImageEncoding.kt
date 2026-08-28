package com.example.scrollbooker.core.util

import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream
import kotlin.math.max
import androidx.core.graphics.scale

const val COVER_DATA_URL_PREFIX = "data:image/jpeg;base64,"

/**
 * Encodes this bitmap as a "data:image/jpeg;base64,..." URI for
 * CreatePostRequest/CreateVideoReviewRequest/UpdatePostRequest's customCover field.
 * Downscaled first since this rides along in a JSON body, not a file upload.
 */
fun Bitmap.toCoverDataUri(maxDimension: Int = 1080, quality: Int = 85): String {
    val largestSide = max(width, height)

    val scaled = if (largestSide > maxDimension) {
        val scale = maxDimension / largestSide.toFloat()
        this.scale(
            (width * scale).toInt().coerceAtLeast(1),
            (height * scale).toInt().coerceAtLeast(1)
        )
    } else this

    val output = ByteArrayOutputStream()
    scaled.compress(Bitmap.CompressFormat.JPEG, quality, output)
    val base64 = Base64.encodeToString(output.toByteArray(), Base64.NO_WRAP)

    return "$COVER_DATA_URL_PREFIX$base64"
}
