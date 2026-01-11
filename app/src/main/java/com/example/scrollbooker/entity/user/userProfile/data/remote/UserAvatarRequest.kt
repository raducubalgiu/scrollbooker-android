package com.example.scrollbooker.entity.user.userProfile.data.remote

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns

class UserAvatarRequest(
    val bytes: ByteArray,
    val fileName: String,
    val mimeType: String
)

fun Uri.toUserAvatarRequest(contentResolver: ContentResolver): UserAvatarRequest {
    val mimeType = contentResolver.getType(this) ?: "image/jpeg"

    val fileName = contentResolver.query(this, null, null, null, null)?.use { cursor ->
        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (nameIndex >= 0 && cursor.moveToFirst()) cursor.getString(nameIndex) else null
    } ?: "avatar.jpg"

    val bytes = contentResolver.openInputStream(this)?.use { it.readBytes() }
        ?: error("Could not open input stream for uri=$this")

    return UserAvatarRequest(
        bytes = bytes,
        fileName = fileName,
        mimeType = mimeType
    )
}