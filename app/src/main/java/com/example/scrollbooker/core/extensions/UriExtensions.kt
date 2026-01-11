package com.example.scrollbooker.core.extensions
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File
import java.io.FileOutputStream

fun Context.getFileName(uri: Uri): String {
    val cursor = contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val idx = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (idx >= 0) return it.getString(idx)
        }
    }
    return "upload_${System.currentTimeMillis()}.mp4"
}

fun uriToCacheFile(context: Context, uri: Uri): File {
    val name = context.getFileName(uri)
    val outFile = File(context.cacheDir, name)

    context.contentResolver.openInputStream(uri)?.use { input ->
        FileOutputStream(outFile).use { output ->
            input.copyTo(output)
        }
    } ?: error("Cannot open input stream for uri=$uri")

    return outFile
}
