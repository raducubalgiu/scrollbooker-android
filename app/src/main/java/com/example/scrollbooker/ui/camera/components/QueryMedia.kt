package com.example.scrollbooker.ui.camera.components

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore

enum class MediaFilter { ALL, PHOTOS, VIDEOS }
enum class MediaType { PHOTO, VIDEO }

data class MediaFile(
    val id: Long,
    val uri: Uri,
    val mimeType: String?,
    val dateAddedSeconds: Long,
    val durationMs: Long?,
    val type: MediaType
)

fun queryMedia(
    context: Context,
    filter: MediaFilter
): List<MediaFile> {
    val collection = MediaStore.Files.getContentUri("external")

    val projection = arrayOf(
        MediaStore.Files.FileColumns._ID,
        MediaStore.Files.FileColumns.MEDIA_TYPE,
        MediaStore.Files.FileColumns.MIME_TYPE,
        MediaStore.MediaColumns.DATE_ADDED,
        MediaStore.Video.VideoColumns.DURATION // pentru imagini e null
    )

    val (selection, args) = when (filter) {
        MediaFilter.ALL -> {
            "${MediaStore.Files.FileColumns.MEDIA_TYPE}=? OR " +
                    "${MediaStore.Files.FileColumns.MEDIA_TYPE}=?" to arrayOf(
                MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
                MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()
            )
        }
        MediaFilter.PHOTOS -> {
            "${MediaStore.Files.FileColumns.MEDIA_TYPE}=?" to arrayOf(
                MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString()
            )
        }
        MediaFilter.VIDEOS -> {
            "${MediaStore.Files.FileColumns.MEDIA_TYPE}=?" to arrayOf(
                MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()
            )
        }
    }

    val sortOrder = "${MediaStore.MediaColumns.DATE_ADDED} DESC"

    val items = mutableListOf<MediaFile>()
    context.contentResolver.query(
        collection, projection, selection, args, sortOrder
    )?.use { cursor ->
        val idCol = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
        val typeCol = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE)
        val mimeCol = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE)
        val dateCol = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_ADDED)
        val durCol = cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DURATION)

        while (cursor.moveToNext()) {
            val id = cursor.getLong(idCol)
            val mediaTypeInt = cursor.getInt(typeCol)
            val mime = cursor.getString(mimeCol)
            val dateAdded = cursor.getLong(dateCol)
            val dur = if (mediaTypeInt == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)
                cursor.getLong(durCol) else null

            val contentUri = when (mediaTypeInt) {
                MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE ->
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO ->
                    ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)
                else -> null
            } ?: continue

            val type = if (dur != null) MediaType.VIDEO else MediaType.PHOTO

            items.add(
                MediaFile(
                    id = id,
                    uri = contentUri,
                    mimeType = mime,
                    dateAddedSeconds = dateAdded,
                    durationMs = dur,
                    type = type
                )
            )
        }
    }

    return items
}