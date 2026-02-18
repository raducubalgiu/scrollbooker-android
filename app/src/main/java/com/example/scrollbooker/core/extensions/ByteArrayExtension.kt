package com.example.scrollbooker.core.extensions

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

fun ByteArray.toImageBitmap(): ImageBitmap {
    return BitmapFactory
        .decodeByteArray(this, 0, size)
        .asImageBitmap()
}