package com.example.scrollbooker.entity.permission.domain.repository

import android.content.Intent
import android.net.Uri
import androidx.paging.Pager
import androidx.paging.PagingSource
import com.example.scrollbooker.entity.permission.domain.model.GalleryItem
import com.example.scrollbooker.entity.permission.domain.model.MediaLibraryAccess
import com.example.scrollbooker.entity.permission.domain.model.MediaVideo
import com.example.scrollbooker.ui.camera.components.MediaFile

interface PermissionRepository {
    fun getMediaLibraryAccess(): MediaLibraryAccess

    fun wasMediaPermissionRequestedOnce(): Boolean
    fun markMediaPermissionRequestedOnce()

    suspend fun getLatestVideoThumbUriOrNull(): Uri?
    fun buildAppSettingsIntent(): Intent

    suspend fun videosPagingSource(pageSize: Int): Pager<Int, MediaVideo>
}