package com.example.scrollbooker.entity.permission.data.repository

import android.Manifest
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import androidx.core.content.ContextCompat
import com.example.scrollbooker.entity.permission.domain.model.MediaLibraryAccess
import com.example.scrollbooker.entity.permission.domain.repository.PermissionRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import androidx.core.content.edit
import androidx.core.net.toUri
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.scrollbooker.entity.permission.data.remote.VideosPagingSource
import com.example.scrollbooker.entity.permission.domain.model.MediaVideo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PermissionRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
): PermissionRepository {
    private val resolver: ContentResolver = context.contentResolver

    private val prefs by lazy {
        context.getSharedPreferences("permissions_prefs", Context.MODE_PRIVATE)
    }

    override fun wasMediaPermissionRequestedOnce(): Boolean =
        prefs.getBoolean(KEY_MEDIA_REQUESTED_ONCE, false)

    override fun markMediaPermissionRequestedOnce() {
        prefs.edit { putBoolean(KEY_MEDIA_REQUESTED_ONCE, true) }
    }

    override fun getMediaLibraryAccess(): MediaLibraryAccess {
        fun granted(p: String): Boolean =
            ContextCompat.checkSelfPermission(context, p) == PackageManager.PERMISSION_GRANTED

        return when {
            Build.VERSION.SDK_INT >= 34 -> {
                val full = granted(Manifest.permission.READ_MEDIA_VIDEO)
                val limited = granted(Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED)
                when {
                    full -> MediaLibraryAccess.FULL
                    limited -> MediaLibraryAccess.LIMITED
                    else -> MediaLibraryAccess.NONE
                }
            }

            Build.VERSION.SDK_INT >= 33 -> {
                if (granted(Manifest.permission.READ_MEDIA_VIDEO)) {
                    MediaLibraryAccess.FULL
                } else {
                    MediaLibraryAccess.NONE
                }
            }

            else -> {
                @Suppress("DEPRECATION")
                if (granted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    MediaLibraryAccess.FULL
                } else {
                    MediaLibraryAccess.NONE
                }
            }
        }
    }

    override suspend fun getLatestVideoThumbUriOrNull(): Uri? = withContext(Dispatchers.IO) {
        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DATE_ADDED
        )

        val collection = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val sortOrder = "${MediaStore.Video.Media.DATE_ADDED} DESC"

        context.contentResolver.query(
            collection,
            projection,
            null,
            null,
            sortOrder
        )?.use { cursor ->
            if (!cursor.moveToFirst()) return@withContext null
            val idCol = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val id = cursor.getLong(idCol)
            ContentUris.withAppendedId(collection, id)
        }
    }

    override fun buildAppSettingsIntent(): Intent =
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = "package:${context.packageName}".toUri()
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

    override suspend fun videosPagingSource(pageSize: Int): Pager<Int, MediaVideo> {
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                initialLoadSize = pageSize * 2,
                prefetchDistance = pageSize / 2,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                VideosPagingSource(context = context, pageSize = pageSize)
            }
        )
    }


    private companion object {
        const val KEY_MEDIA_REQUESTED_ONCE = "media_requested_once"
    }
}